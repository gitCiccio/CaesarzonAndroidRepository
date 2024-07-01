package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.caesarzonapplication.model.service.KeycloakService
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import com.example.caesarzonapplication.viewmodels.AccountInfoViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun LoginPopup(onDismiss: () -> Unit, onLoginSuccess: () -> Unit, navController: NavController, accountInfoViewModel: AccountInfoViewModel){
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var otp by rememberSaveable { mutableStateOf("") }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    var showPopup by rememberSaveable { mutableStateOf(false) }
    var showPopupMessage by rememberSaveable { mutableStateOf("") }
    var showOtpPopup by rememberSaveable { mutableStateOf(false) }

    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    if (showPopup) { GenericMessagePopup(message = showPopupMessage, onDismiss = { showPopup = false }) }

    if (showOtpPopup) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Text(text = "Recupero password", style = TextStyle(fontSize = 16.sp))
            },
            text = {
                Column {
                    Text("Otp di recupero inviato con successo! Controlla la tua e-mail e inserisci il codice otp qua sotto:")
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = otp,
                        onValueChange = { otp = it },
                        label = { Text("Inserisci codice OTP") }
                    )
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Inserisci nuova password") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    coroutineScope.launch {
                        val responseCode = accountInfoViewModel.verifyOTP(otp,password,username)
                        if (responseCode == "success") {
                            showPopupMessage = "Codice OTP confermato. Password ripristinata con successo."
                            showPopup = true
                            onDismiss()
                        } else {
                            showPopupMessage = "Codice OTP errato"
                            showPopup = true
                        }
                    }
                }) {
                    Text(text = "OK")
                }
            }
        )
    }
    
    if (errorMessage != null) {
        AlertDialog(onDismissRequest = { errorMessage=null }, 
            confirmButton = { 
                Button(modifier = Modifier
                    .padding(30.dp)
                    .fillMaxWidth()
                    .height(50.dp)
                    , onClick = { errorMessage = null }) {
                    Text(text = "OK")
                }
            },
            title = { Text(modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
                text = "Errore durante il login",
                softWrap = false) },
            text = { Text(modifier = Modifier
                .fillMaxWidth(),
                text = errorMessage ?: "",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
                )
            }
        )
    }

    AlertDialog(
          modifier = Modifier.height(420.dp),
          onDismissRequest = onDismiss,
          confirmButton = {
                  Button(
                      colors = ButtonColors(Color(238, 137, 60, 255), Color.White, Color.White, Color.White) ,
                      onClick = {
                          GlobalScope.launch(Dispatchers.IO){
                              try{
                                  KeycloakService().getAccessToken(username, password)
                                  if (myToken != null) {
                                      if (accountInfoViewModel.getUserData() == "success") {
                                          onLoginSuccess()
                                      } else {
                                          errorMessage = "Username o password errati."
                                      }
                                  } else {
                                      errorMessage = "Token dell'ospite non valido. Contatta l'assistenza per ricevere supporto su questo errore."}
                              }catch (e: Exception){
                                  e.printStackTrace()
                                  errorMessage = "Errore durante il login. Contatta l'assistenza per ricevere supporto su questo errore."
                              }
                          }
                      }
                  ){
                      Text(text = "Accedi")
                  }
          },
          dismissButton = { Button(onClick = {navController.navigate("home"); onDismiss(); })
          { Text(text = "Annulla") } },
          title = { Text(modifier = Modifier
              .fillMaxWidth()
              .wrapContentSize()
              .height(40.dp)
              , text = "Login") },
          text = {
              Column {
                  TextField(
                      value = username,
                      onValueChange = { username = it },
                      label = { Text(text = "Username") })
                  TextField(
                      value = password,
                      onValueChange = { password = it },
                      label = { Text(text = "Password") },
                      visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                      trailingIcon = {
                          val image = if (passwordVisible)
                              Icons.Default.Visibility
                          else
                              Icons.Default.VisibilityOff

                          IconButton(onClick = {
                              passwordVisible = !passwordVisible
                          }) {
                              Icon(image, contentDescription = null)
                          }
                      }
                  )
                  TextButton(
                      modifier = Modifier.padding(vertical = 10.dp),
                      onClick = { navController.navigate("register"); onDismiss(); },
                      ) {
                      Text(text = "Non sei registrato? Clicca qui per registrarti",style = TextStyle(fontSize = 16.sp))
                  }
                  TextButton(
                      onClick = {
                          if (username.isNotEmpty()) {
                                coroutineScope.launch {
                                    val responseFromPasswordRecovery = accountInfoViewModel.retrieveForgottenPassword(username)
                                    if (responseFromPasswordRecovery == "success") {
                                        showOtpPopup = true
                                    }
                                    else{
                                        showPopupMessage = "Problemi nell'invio dell'otp per il recupero della password. Username non valido"
                                        showPopup = true
                                    }
                                }}
                          else{
                              showPopupMessage = "Si prega di inserire uno username valido"
                              showPopup = true
                          }
                      })
                  {
                      Text(text = "Password dimenticata? Clicca qui per resettarla",style = TextStyle(fontSize = 16.sp))
                  }
              }
          }
      )
}


