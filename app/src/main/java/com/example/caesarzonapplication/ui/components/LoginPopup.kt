package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
    var loginFailed by rememberSaveable { mutableStateOf(false) }

    AlertDialog(
          onDismissRequest = onDismiss,
          confirmButton = {
                  Button(
                      onClick = {
                          GlobalScope.launch(Dispatchers.IO){
                              try{
                                   KeycloakService().getAccessToken(username, password)
                                  if (myToken != null){
                                      println("access: "+myToken?.accessToken)
                                      onLoginSuccess()
                                  }else{
                                      loginFailed = true
                                  }
                              }catch (e: Exception){
                                  e.printStackTrace()
                              }
                          }
                      }
                  ){
                      Text(text = "Accedi")
                  }
          },
          dismissButton = { Button(onClick = {navController.navigate("home"); onDismiss(); }){ Text(text = "Annulla") } },
          title = { Text(text = "Login") },
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
              }
          }
      )
}


