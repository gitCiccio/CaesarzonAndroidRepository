package com.example.caesarzonapplication.ui.screens.adminScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AccountInfoViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AccountInfoViewModel.Companion.userDataForAdmin
import com.example.caesarzonapplication.ui.components.GenericMessagePopup

@Composable
fun UserProfileAdminScreen(accountInfoViewModel: AccountInfoViewModel) {

    var username by rememberSaveable { mutableStateOf(userDataForAdmin?.username ?: "") }
    var firstName by rememberSaveable { mutableStateOf(userDataForAdmin?.firstName ?: "") }
    var lastName by rememberSaveable { mutableStateOf(userDataForAdmin?.lastName ?: "") }
    var email by rememberSaveable { mutableStateOf(userDataForAdmin?.email ?: "") }
    var phoneNumber by rememberSaveable { mutableStateOf(userDataForAdmin?.phoneNumber ?: "") }

    LaunchedEffect(Unit) {
        userDataForAdmin?.let {
            username = it.username
            firstName = it.firstName
            lastName = it.lastName
            email = it.email
            phoneNumber = it.phoneNumber
        }
    }

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
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Profilo dell'utente $username",
                style = MaterialTheme.typography.headlineLarge
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                enabled = isUserInfoTextFieldEnabled,
                value = firstName,
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
                value = lastName,
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
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                enabled = isUserInfoTextFieldEnabled,
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                enabled = isUserInfoTextFieldEnabled,
                value = phoneNumber,
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
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    if (isUserInfoTextFieldEnabled) {
                        accountInfoViewModel.modifyUserDataByAdmin(
                            firstName,
                            lastName,
                            username,
                            email,
                            phoneNumber
                        ) {
                            if (it == "success") {
                                showPopupMessage = "Dati modificati con successo"
                                showPopup = true
                                userDataForAdmin?.username = username
                                userDataForAdmin?.firstName = firstName
                                userDataForAdmin?.lastName = lastName
                                userDataForAdmin?.email = email
                                userDataForAdmin?.phoneNumber = phoneNumber
                            } else {
                                showPopupMessage = "Errore durante la modifica dei dati"
                                showPopup = true
                            }
                        }
                        isUserInfoTextFieldEnabled = false
                    } else {
                        isUserInfoTextFieldEnabled = true
                    }
                }) {
                    Text(text = if (isUserInfoTextFieldEnabled) "Salva" else "Modifica Info")
                }
            }
        }
    }
}
