package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AccountInfoViewModel
import com.example.caesarzonapplication.navigation.BottomBarScreen

@Composable
fun UserRegistrationScreen(
    navController: NavHostController,
    accountInfoViewModel: AccountInfoViewModel
) {

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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Text(
                text = "Registrazione",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp),
            color = MaterialTheme.colorScheme.primary
            )
        }
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
                {
                    InputField(
                        value = name,
                        onValueChange = {
                            name = it
                            nameError = validateName(it)
                        },
                        label = "Nome",
                        error = nameError
                )

                InputField(
                    value = surname,
                    onValueChange = {
                        surname = it
                        surnameError = validateSurname(it)
                    },
                    label = "Cognome",
                    error = surnameError
                )

                InputField(
                    value = username,
                    onValueChange = {
                        username = it
                        usernameError = validateUsername(it)
                    },
                    label = "Username",
                    error = usernameError
                )

                InputField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = validateEmail(it)
                    },
                    label = "Email",
                    error = emailError
                )

                PasswordInputField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = validatePassword(it)
                    },
                    label = "Password",
                    error = passwordError
                )

                PasswordInputField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        confirmPasswordError = validateConfirmPassword(it, password)
                    },
                    label = "Conferma Password",
                    error = confirmPasswordError
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (checksAreOk(nameError, surnameError, usernameError, emailError, passwordError)) {
                            accountInfoViewModel.registerUser(name, surname, username, email, password) { result ->
                                if (result == "success") {
                                    navController.navigate(BottomBarScreen.Profile.route)
                                } else {
                                    println("Errore durante la registrazione: $result")
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .height(75.dp)
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    )
                )
                {
                    Text("Registrati", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                    Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            isError = error.isNotEmpty(),
            singleLine = true
        )
        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
fun PasswordInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = error.isNotEmpty(),
            singleLine = true,
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = {
                    passwordVisible = !passwordVisible
                }) {
                    Icon(imageVector = image, contentDescription = null)
                }
            }
        )
        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}


fun validateName(name: String): String {
    return when {
        name.isEmpty() -> "Il nome non può essere vuoto"
        name.length < 2 || name.length > 30 -> "Il nome deve essere tra 2 e 30 caratteri"
        !name[0].isUpperCase() -> "Il nome deve iniziare con una lettera maiuscola"
        else -> ""
    }
}

fun validateSurname(surname: String): String {
    return when {
        surname.isEmpty() -> "Il cognome non può essere vuoto"
        surname.length < 2 || surname.length > 30 -> "Il cognome deve essere tra 2 e 30 caratteri"
        !surname[0].isUpperCase() -> "Il cognome deve iniziare con una lettera maiuscola"
        else -> ""
    }
}

fun validateUsername(username: String): String {
    return when {
        username.isEmpty() -> "L'username non può essere vuoto"
        username.length < 5 || username.length > 20 -> "L'username deve essere tra 5 e 20 caratteri"
        else -> ""
    }
}

fun validateEmail(email: String): String {
    return when {
        email.isEmpty() -> "L'email non può essere vuota"
        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Inserisci un'email valida"
        email.substringBefore('@').length > 64 -> "La lunghezza della parte locale dell'email deve essere inferiore a 64 caratteri"
        else -> ""
    }
}

fun validatePassword(password: String): String {
    return when {
        password.isEmpty() -> "La password non può essere vuota"
        password.length < 8 || password.length > 20 -> "La password deve essere tra 8 e 20 caratteri"
        !password.any { it.isUpperCase() } -> "La password deve contenere almeno una lettera maiuscola"
        !password.any { it.isDigit() } -> "La password deve contenere almeno un numero"
        !password.any { !it.isLetterOrDigit() } -> "La password deve contenere almeno un carattere speciale"
        else -> ""
    }
}

fun validateConfirmPassword(confirmPassword: String, password: String): String {
    return when {
        confirmPassword.isEmpty() -> "La conferma della password non può essere vuota"
        confirmPassword != password -> "Le password non coincidono"
        else -> ""
    }
}

fun checksAreOk(
    nameError: String,
    surnameError: String,
    usernameError: String,
    emailError: String,
    passwordError: String
): Boolean {
    return nameError.isEmpty() && surnameError.isEmpty() && usernameError.isEmpty() && emailError.isEmpty() && passwordError.isEmpty()
}
