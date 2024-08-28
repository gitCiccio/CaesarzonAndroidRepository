package com.example.caesarzonapplication.ui.components

import android.graphics.ImageDecoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.caesarzonapplication.model.viewmodels.ProductsViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AccountInfoViewModel
import kotlinx.coroutines.launch

@Composable
fun UserInfoSection(accountInfoViewModel: AccountInfoViewModel) {

    var username by remember { mutableStateOf(accountInfoViewModel.userData?.username) }
    var firstName by remember { mutableStateOf(accountInfoViewModel.userData?.firstName) }
    var lastName by remember { mutableStateOf(accountInfoViewModel.userData?.lastName) }
    var email by remember { mutableStateOf(accountInfoViewModel.userData?.email) }
    var phoneNumber by remember { mutableStateOf(accountInfoViewModel.userData?.phoneNumber) }
    var password by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    var isPasswordTextFieldEnabled by remember { mutableStateOf(false) }
    var isUserInfoTextFieldEnabled by remember { mutableStateOf(false) }

    var showPopup by rememberSaveable { mutableStateOf(false) }
    var showPopupMessage by rememberSaveable { mutableStateOf("") }

    if (showPopup) {
        GenericMessagePopup(message = showPopupMessage, onDismiss = { showPopup = false })
    }

    var firstNameError by remember { mutableStateOf("") }
    var lastNameError by remember { mutableStateOf("") }
    var phoneNumberError by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        item {
            accountInfoViewModel.profileImage.value?.profilePicture?.asImageBitmap()?.let {
                Image(
                    bitmap = it,
                    contentDescription = "User Profile",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(16.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .padding(4.dp)
                        .background(Color.White, CircleShape)
                )
            } ?: Image(
                imageVector = Icons.Default.Person,
                contentDescription = "User Profile",
                modifier = Modifier
                    .size(120.dp)
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                    .padding(4.dp)
                    .background(Color.White, CircleShape)
            )
            Button(onClick = { /*accountInfoViewModel.updateImageProfile()*/ }) 
           {
                Text("Carica Immagine")
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                enabled = isUserInfoTextFieldEnabled,
                value = firstName ?: "",
                onValueChange = {
                    firstName = it
                    firstNameError = if (it.isNotEmpty() && it.first().isLowerCase()) {
                        "La prima lettera deve essere maiuscola"
                    } else {
                        ""
                    }
                },
                label = { Text("Nome") },
                isError = firstNameError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            if (firstNameError.isNotEmpty()) {
                Text(firstNameError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                enabled = isUserInfoTextFieldEnabled,
                value = lastName ?: "",
                onValueChange = {
                    lastName = it
                    lastNameError = if (it.isNotEmpty() && it.first().isLowerCase()) {
                        "La prima lettera deve essere maiuscola"
                    } else {
                        ""
                    }
                },
                label = { Text("Cognome") },
                isError = lastNameError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            if (lastNameError.isNotEmpty()) {
                Text(lastNameError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                enabled = false,
                value = username ?: "",
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                enabled = isUserInfoTextFieldEnabled,
                value = email ?: "",
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                enabled = isUserInfoTextFieldEnabled,
                value = phoneNumber ?: "",
                onValueChange = {
                    phoneNumber = it
                    phoneNumberError = if (it.length != 10) {
                        "Deve essere di 10 caratteri"
                    } else {
                        ""
                    }
                },
                label = { Text("Numero di telefono") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = phoneNumberError.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            )
            if (phoneNumberError.isNotEmpty()) {
                Text(
                    phoneNumberError,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                enabled = isPasswordTextFieldEnabled,
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        if (isPasswordTextFieldEnabled) {
                            coroutineScope.launch {
                                username?.let {
                                    accountInfoViewModel.changePassword(password, it, 1) { responseCode ->
                                        if (responseCode == "success") {
                                            password = ""
                                            showPopupMessage = "Password modificata con successo"
                                        } else {
                                            showPopupMessage = "Errore durante la modifica della password"
                                        }
                                        showPopup = true
                                    }
                                }
                            }
                        }
                        isPasswordTextFieldEnabled = !isPasswordTextFieldEnabled
                    }
                ) {
                    Text(text = if (isPasswordTextFieldEnabled) "Conferma Password" else "Modifica Password")
                }

                Button(onClick = {
                    if (isUserInfoTextFieldEnabled) {
                        accountInfoViewModel.modifyUserData(
                            firstName ?: "",
                            lastName ?: "",
                            username ?: "",
                            email ?: "",
                            phoneNumber ?: ""
                        ) {
                            if (it == "success") {
                                showPopupMessage = "Dati modificati con successo"
                                showPopup = true
                            } else
                                showPopupMessage = "Errore durante la modifica dei dati"
                        }
                        isUserInfoTextFieldEnabled = false
                    } else
                        isUserInfoTextFieldEnabled = true
                }) {
                    Text(text = if (isUserInfoTextFieldEnabled) "Salva" else "Modifica Info")
                }
            }
        }
    }
}