package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.input.TextFieldValue
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

    var addresses by remember { mutableStateOf(listOf("")) }
    var selectedAddress by rememberSaveable { mutableStateOf(addresses[0]) }
    var showAddAddressDialog by rememberSaveable { mutableStateOf(false) }
    var showRemoveAddressDialog by rememberSaveable { mutableStateOf(false) }
    var addressDropdownExpanded by rememberSaveable { mutableStateOf(false) }
    var isPasswordTextFieldEnabled by remember { mutableStateOf(false) }
    var isUserInfoTextFieldEnabled by remember { mutableStateOf(false) }

    var showPopup by rememberSaveable { mutableStateOf(false) }
    var showPopupMessage by rememberSaveable { mutableStateOf("") }

    if (showPopup) { GenericMessagePopup(message = showPopupMessage, onDismiss = { showPopup = false }) }

    var firstNameError by remember { mutableStateOf("") }
    var lastNameError by remember { mutableStateOf("") }
    var phoneNumberError by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        item {
            firstName?.let {
                TextField(
                    enabled = isUserInfoTextFieldEnabled,
                    value = it,
                    onValueChange = {
                        firstName = it
                        firstNameError = if (it.isNotEmpty() && it.first().isLowerCase()) {
                            "Il nome deve avere la prima lettera maiuscola"
                        } else {
                            ""
                        }
                    },
                    label = { Text("Nome") },
                    isError = firstNameError.isNotEmpty()
                )
            }
            if (firstNameError.isNotEmpty()) {
                Text(firstNameError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
            }
        }

        item {
            lastName?.let {
                TextField(
                    enabled = isUserInfoTextFieldEnabled,
                    value = it,
                    onValueChange = {
                        lastName = it
                        lastNameError = if (it.isNotEmpty() && it.first().isLowerCase()) {
                            "Il cognome deve avere la prima lettera maiuscola"
                        } else {
                            ""
                        }
                    },
                    label = { Text("Cognome") },
                    isError = lastNameError.isNotEmpty()
                )
            }
            if (lastNameError.isNotEmpty()) {
                Text(lastNameError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
            }
        }

        item {
            username?.let {
                TextField(
                    enabled = false,
                    value = it,
                    onValueChange = { username = it },
                    label = { Text("Username") }
                )
            }
        }

        item {
            email?.let {
                TextField(
                    enabled = isUserInfoTextFieldEnabled,
                    value = it,
                    onValueChange = { email = it },
                    label = { Text("Email") }
                )
            }
        }

        item {
            phoneNumber?.let {
                TextField(
                    enabled = isUserInfoTextFieldEnabled,
                    value = it,
                    onValueChange = {
                        phoneNumber = it
                        phoneNumberError = if (it.length != 10) {
                            "Il numero di telefono deve essere composto da 10 caratteri"
                        } else {
                            ""
                        }
                    },
                    label = { Text("Numero di telefono") },
                    isError = phoneNumberError.isNotEmpty()
                )
            }
            if (phoneNumberError.isNotEmpty()) {
                Text(phoneNumberError, color = Color.Red, style = MaterialTheme.typography.bodySmall)
            }
        }

        item {
            TextField(
                value = password,
                onValueChange = { password = it },
                enabled = isPasswordTextFieldEnabled,
                label = { Text("Password") }
            )
        }

        item {
            Button(onClick = {
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
            })
            {
                Text(text = if (isPasswordTextFieldEnabled) "Conferma password" else "Modifica password")
            }
            Box {
                TextField(
                    value = TextFieldValue(selectedAddress),
                    onValueChange = {},
                    label = { Text("Indirizzo di spedizione") },
                    enabled = false,
                    trailingIcon = {
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "Drop-down arrow")
                    },
                    modifier = Modifier
                        .clickable { addressDropdownExpanded = true }
                )
                DropdownMenu(
                    expanded = addressDropdownExpanded,
                    onDismissRequest = { addressDropdownExpanded = false },
                    modifier = Modifier.width(280.dp)
                ) {
                    addresses.forEach { address ->
                        DropdownMenuItem(
                            text = { Text(text = address) },
                            onClick = {
                                selectedAddress = address
                                addressDropdownExpanded = false
                            }
                        )
                    }
                }
            }
            Row {
                Button(onClick = {
                    if (isUserInfoTextFieldEnabled) {
                        firstName?.let {
                            lastName?.let { it1 ->
                                phoneNumber?.let { it2 ->
                                    username?.let { it3 ->
                                        email?.let { it4 ->
                                            accountInfoViewModel.modifyUserData(it, it1, it3, it2, it4) { responseCode ->
                                                showPopupMessage = if (responseCode == "success") {
                                                    "Informazioni modificate con successo"
                                                } else {
                                                    "Errore durante la modifica delle informazioni"
                                                }
                                                showPopup = true
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        isUserInfoTextFieldEnabled = !isUserInfoTextFieldEnabled
                    } else {
                        isUserInfoTextFieldEnabled = true
                    }
                }) {
                    Text(text = "Modifica i tuoi dati")
                }
            }
        }
    }

    if (showAddAddressDialog) {
        Dialog(onDismissRequest = { showAddAddressDialog = false }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = MaterialTheme.shapes.medium)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = "Aggiungi Indirizzo",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Chiudi",
                            modifier = Modifier
                                .clickable { showAddAddressDialog = false }
                        )
                    }
                    var street by rememberSaveable { mutableStateOf(TextFieldValue("Via Ina Casa")) }
                    var houseNumber by rememberSaveable { mutableStateOf(TextFieldValue("N° 81")) }
                    var city by rememberSaveable { mutableStateOf(TextFieldValue("Luzzi")) }
                    var zipCode by rememberSaveable { mutableStateOf(TextFieldValue("87040")) }
                    var province by rememberSaveable { mutableStateOf(TextFieldValue("Cosenza")) }
                    var region by rememberSaveable { mutableStateOf(TextFieldValue("Calabria")) }

                    TextField(
                        value = street,
                        onValueChange = { street = it },
                        label = { Text("Via") }
                    )
                    TextField(
                        value = houseNumber,
                        onValueChange = { houseNumber = it },
                        label = { Text("Numero Civico") }
                    )
                    TextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("Città") }
                    )
                    TextField(
                        value = zipCode,
                        onValueChange = { zipCode = it },
                        label = { Text("CAP") }
                    )
                    TextField(
                        value = province,
                        onValueChange = { province = it },
                        label = { Text("Provincia") }
                    )
                    TextField(
                        value = region,
                        onValueChange = { region = it },
                        label = { Text("Regione") }
                    )
                    Button(onClick = {
                        val newAddress = "${street.text} ${houseNumber.text}, ${city.text}, ${zipCode.text}, ${province.text}, ${region.text}"
                        addresses = addresses + newAddress
                        selectedAddress = newAddress
                        showAddAddressDialog = false
                    }) {
                        Text(text = "Salva")
                    }
                }
            }
        }
    }

    if (showRemoveAddressDialog) {
        Dialog(onDismissRequest = { showRemoveAddressDialog = false }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color.White, shape = MaterialTheme.shapes.medium)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = "Rimuovi Indirizzo",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Chiudi",
                            modifier = Modifier
                                .clickable { showRemoveAddressDialog = false }
                        )
                    }
                    addresses.forEach { address ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = address, modifier = Modifier.weight(1f))
                            IconButton(onClick = {
                                addresses = addresses - address
                                if (selectedAddress == address && addresses.isNotEmpty()) {
                                    selectedAddress = addresses[0]
                                }
                                if (addresses.isEmpty()) {
                                    selectedAddress = ""
                                }
                            }) {
                                Icon(
                                    Icons.Filled.Close,
                                    contentDescription = "Rimuovi"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}