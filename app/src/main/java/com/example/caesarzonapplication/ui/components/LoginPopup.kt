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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

@Composable
fun LoginPopup(onDismiss: () -> Unit, onLoginSuccess: () -> Unit, navController: NavController){
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var loginFailed by rememberSaveable { mutableStateOf(false) }
    //val tokenViewModel: TokenViewModel = viewModel()

    AlertDialog(
          onDismissRequest = onDismiss,
          confirmButton = {
              Button(
                  onClick = {
                      GlobalScope.launch(Dispatchers.IO){
                          try{
                              val response = login("cesare", "baby12345")
                              println(response)
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
                      value = "",
                      onValueChange = {},
                      label = { Text(text = "Username") })
                  TextField(
                      value = "",
                      onValueChange = {},
                      label = { Text(text = "Password") },
                      visualTransformation = PasswordVisualTransformation())
              }
          }
      )
}

suspend fun login(username: String, password: String): String {
    val url = URL("http://25.29.186.68:8080/realms/CaesarRealm/protocol/openid-connect/token")
    val connection = url.openConnection() as HttpURLConnection

    connection.requestMethod = "POST"
    connection.doOutput = true
    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

    val postData =
        "username=${URLEncoder.encode(username, "UTF-8")}"+
                "&password=${URLEncoder.encode(password, "UTF-8")}"+
                "&grant_type=password"+
                "&client_id=caesar-app"
    val outputStream = OutputStreamWriter(connection.outputStream)
    outputStream.write(postData)
    outputStream.flush()

    val responseCode = connection.responseCode
    val inputStream: InputStream = if(responseCode == HttpURLConnection.HTTP_OK){
        connection.inputStream
    }else{
        connection.errorStream
    }

    val reader = BufferedReader(
        InputStreamReader(inputStream))

    val response = StringBuilder()
    var line: String?
    while(reader.readLine().also { line = it } != null){
        response.append(line)
    }
    reader.close()
    connection.disconnect()

    return response.toString()

}