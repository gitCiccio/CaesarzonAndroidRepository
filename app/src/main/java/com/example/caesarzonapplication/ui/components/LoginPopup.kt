package com.example.caesarzonapplication.ui.components

import android.widget.Button
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@Composable
fun LoginPopup(onDismiss: () -> Unit, onLoginSuccess: () -> Unit, navController: NavController){
      AlertDialog(
          onDismissRequest = onDismiss,
          confirmButton = {
              Button(onClick = { onLoginSuccess(); onDismiss(); }){ Text(text = "Accedi") }
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