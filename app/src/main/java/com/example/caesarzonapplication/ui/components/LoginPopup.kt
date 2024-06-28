package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.caesarzonapplication.model.service.KeycloakService
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import com.example.caesarzonapplication.viewmodels.AccountInfoViewModel
import com.example.caesarzonapplication.viewmodels.FollowersAndFriendsViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun LoginPopup(onDismiss: () -> Unit, onLoginSuccess: () -> Unit, navController: NavController){
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    
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
                                  if (KeycloakService.myToken != null) {
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
          dismissButton = { Button(onClick = {navController.navigate("home"); onDismiss(); }){ Text(text = "Annulla") } },
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
                      onValueChange = {password = it},
                      label = { Text(text = "Password") },
                      visualTransformation = PasswordVisualTransformation()
                  )
                  TextButton(
                      modifier = Modifier.padding(vertical = 10.dp),
                      onClick = { /*TODO*/ },
                      ) {
                      Text(text = "Non sei registrato? Clicca qui per registrarti")
                  }
                  TextButton(
                      onClick = { /*TODO*/ },
                  ) {
                      Text(text = "Password dimenticata? Clicca qui per resettarla")
                  }
              }
          }
      )
}


