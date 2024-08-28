package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.basicToken
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AccountInfoViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.FollowersViewModel
import com.example.caesarzonapplication.navigation.BottomBarScreen
import com.example.caesarzonapplication.navigation.DetailsScreen
import com.example.caesarzonapplication.ui.components.GenericMessagePopup
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    navController: NavController,
    accountInfoViewModel: AccountInfoViewModel,
    followerViewModel: FollowersViewModel,
    logged: MutableState<Boolean>
) {


    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var otp by rememberSaveable { mutableStateOf("") }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    var showPopup by rememberSaveable { mutableStateOf(false) }
    var showPopupMessage by rememberSaveable { mutableStateOf("") }
    var showOtpPopup by rememberSaveable { mutableStateOf(false) }

    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    if (showPopup) {
        GenericMessagePopup(message = showPopupMessage, onDismiss = { showPopup = false })
    }

    if (showOtpPopup) {
        OtpDialog(
            otp = otp,
            password = password,
            onOtpChange = { otp = it },
            onPasswordChange = { password = it },
            onConfirm = {
                coroutineScope.launch {
                    accountInfoViewModel.verifyOTP(otp, password, username) { responseCode ->
                        if (responseCode == "success") {
                            showPopupMessage = "Codice OTP confermato. Password ripristinata con successo."
                            showPopup = true
                            navController.navigate("home")
                        } else {
                            showPopupMessage = "Codice OTP errato"
                            showPopup = true
                        }
                    }
                }
            },
            onDismiss = { showOtpPopup = false }
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Caesarzon",
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .padding(bottom = 30.dp)
        )
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(text = "Username") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(image, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        if (errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        Button(
            onClick = {
                coroutineScope.launch {
                    try {
                        if (basicToken != null) {
                            val success = accountInfoViewModel.login(username, password)
                            if (success) {
                                accountInfoViewModel.getUserData()
                                followerViewModel.loadAllFollowers()
                                followerViewModel.loadAllFriends()
                                navController.navigate(BottomBarScreen.Home.route)
                            } else {
                                errorMessage = "Username o password errati."
                            }
                        } else {
                            errorMessage = "Token dell'ospite non valido. Contatta l'assistenza per ricevere supporto su questo errore."
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        errorMessage = "Errore durante il login. Contatta l'assistenza per ricevere supporto su questo errore."
                    }
                }
            },
            modifier = Modifier
                .width(200.dp)
                .padding(vertical = 14.dp)
                .height(65.dp)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        ) {
            Text(text = "Accedi")
        }
        TextButton(
            onClick = { navController.navigate(DetailsScreen.UserRegistrationDetailsScreen.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Non sei registrato? Clicca qui per registrarti",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        TextButton(
            onClick = {
                if (username.isNotEmpty()) {
                    coroutineScope.launch {
                        accountInfoViewModel.retrieveForgottenPassword(username) { responseFromPasswordRecovery ->
                            if (responseFromPasswordRecovery == "success") {
                                showOtpPopup = true
                            } else {
                                showPopupMessage = "Problemi nell'invio dell'OTP per il recupero della password. Username non valido"
                                showPopup = true
                            }
                        }
                    }
                } else {
                    showPopupMessage = "Si prega di inserire uno username valido"
                    showPopup = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Password dimenticata? Clicca qui per resettarla",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun OtpDialog(
    otp: String,
    password: String,
    onOtpChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Recupero password", style = TextStyle(fontSize = 16.sp)) },
        text = {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Otp di recupero inviato con successo! Controlla la tua e-mail e inserisci il codice otp qua sotto:")
                TextField(
                    value = otp,
                    onValueChange = onOtpChange,
                    label = { Text("Inserisci codice OTP") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = { Text("Inserisci nuova password") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Annulla")
            }
        }
    )
}
