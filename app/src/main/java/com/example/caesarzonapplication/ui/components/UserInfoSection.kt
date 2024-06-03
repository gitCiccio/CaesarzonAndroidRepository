package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun UserInfoSection() {
    var name by remember { mutableStateOf(TextFieldValue("John Doe")) }
    var email by remember { mutableStateOf(TextFieldValue("johndoe@example.com")) }
    var address by remember { mutableStateOf(TextFieldValue("123 Main St, Anytown, USA")) }
    var passwordVisible by remember { mutableStateOf(false) }

    Column {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Indirizzo di spedizione") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = if (passwordVisible) TextFieldValue("password123") else TextFieldValue("********"),
                onValueChange = {},
                label = { Text("Password") },
                enabled = false
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { passwordVisible = !passwordVisible }) {
                Text(if (passwordVisible) "Nascondi" else "Mostra")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { /* Logica per salvare le informazioni aggiornate */ }) {
            Text(text = "Aggiorna Profilo")
        }
    }
}
