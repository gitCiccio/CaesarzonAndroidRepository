package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.navigation.NavigationBottomBar
import com.example.caesarzonapplication.model.viewmodels.AccountInfoViewModel
import kotlinx.coroutines.launch

@Composable
fun UserRegistrationScreen(navController: NavHostController, logged: Boolean) {

    if(logged) navController.navigate("home")

    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf("") }
    var surnameError by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Registrazione",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(20.dp),
                    textAlign = TextAlign.Center
                )
                TextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = when {
                            it.isEmpty() -> "Il nome non può essere vuoto"
                            it.length < 2 || it.length > 30 -> "Il nome deve essere tra 2 e 30 caratteri"
                            !it[0].isUpperCase() -> "Il nome deve iniziare con una lettera maiuscola"
                            else -> ""
                        }
                    },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    isError = nameError.isNotEmpty()
                )
                if (nameError.isNotEmpty()) {
                    Text(nameError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                }
                TextField(
                    value = surname,
                    onValueChange = {
                        surname = it
                        surnameError = when {
                            it.isEmpty() -> "Il cognome non può essere vuoto"
                            it.length < 2 || it.length > 30 -> "Il cognome deve essere tra 2 e 30 caratteri"
                            !it[0].isUpperCase() -> "Il cognome deve iniziare con una lettera maiuscola"
                            else -> ""
                        }
                    },
                    label = { Text("Cognome") },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    isError = surnameError.isNotEmpty()
                )
                if (surnameError.isNotEmpty()) {
                    Text(surnameError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                }
               TextField(
                    value = username,
                    onValueChange = {
                        username = it
                        usernameError = when {
                            it.isEmpty() -> "L'username non può essere vuoto"
                            it.length < 5 || it.length > 20 -> "L'username deve essere tra 5 e 20 caratteri"
                            else -> ""
                        }
                    },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    isError = usernameError.isNotEmpty()
               )
                if (usernameError.isNotEmpty()) {
                    Text(usernameError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                }
                TextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = when {
                            it.isEmpty() -> "L'email non può essere vuota"
                            !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches() -> "Inserisci un'email valida"
                            it.substringBefore('@').length > 64 -> "La lunghezza della parte locale dell'email deve essere inferiore a 64 caratteri"
                            else -> ""
                        }
                    },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(0.8f),
                    isError = emailError.isNotEmpty()
                )
                if (emailError.isNotEmpty()) {
                    Text(emailError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                }
                TextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = when {
                            it.isEmpty() -> "La password non può essere vuota"
                            it.length < 8 || it.length > 20 -> "La password deve essere tra 8 e 20 caratteri"
                            !it.any { it.isUpperCase() } -> "La password deve contenere almeno una lettera maiuscola"
                            !it.any { it.isDigit() } -> "La password deve contenere almeno un numero"
                            !it.any { !it.isLetterOrDigit() } -> "La password deve contenere almeno un carattere speciale"
                            else -> ""
                        }
                    },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(0.8f),
                    isError = passwordError.isNotEmpty()
                )
                if (passwordError.isNotEmpty()) {
                    Text(passwordError, modifier = Modifier.padding(horizontal = 45.dp, vertical = 5.dp), color = Color.Red, style = MaterialTheme.typography.bodySmall)
                }
                TextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        confirmPasswordError = when {
                            it.isEmpty() -> "La conferma della password non può essere vuota"
                            it != password -> "Le password non coincidono"
                            else -> ""
                        }
                    },
                    label = { Text("Conferma password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(0.8f),
                    isError = confirmPasswordError.isNotEmpty()
                )
                if (confirmPasswordError.isNotEmpty()) {
                    Text(confirmPasswordError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        item {
            Button(
                onClick = {
                    if (name.isEmpty()) nameError = "Il nome non può essere vuoto"
                    if (surname.isEmpty()) surnameError = "Il cognome non può essere vuoto"
                    if (username.isEmpty()) usernameError = "L'username non può essere vuoto"
                    if (email.isEmpty()) emailError = "L'email non può essere vuota"
                    if (password.isEmpty()) passwordError = "La password non può essere vuota"
                    if (confirmPassword.isEmpty()) confirmPasswordError = "La conferma della password non può essere vuota"

                    if (password != confirmPassword) {
                        passwordError = "Le password non coincidono"
                        confirmPasswordError = "Le password non coincidono"
                        password = ""
                        confirmPassword = ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(20.dp),
                colors = ButtonDefaults.buttonColors()
            ) {
                Text("Registrati")
            }
        }
    }
}
