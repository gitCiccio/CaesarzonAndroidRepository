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
import com.example.caesarzonapplication.viewmodels.AccountInfoViewModel
import kotlinx.coroutines.launch

@Composable
fun UserInfoSection() {

    var user  = AccountInfoViewModel.UserData.accountInfoData.collectAsState()

    var username by remember { mutableStateOf(user.value.username) }
    var firstName by remember { mutableStateOf(user.value.firstName) }
    var lastName by remember { mutableStateOf(user.value.lastName) }
    var email by remember { mutableStateOf(user.value.email) }
    var phoneNumber by remember { mutableStateOf(user.value.phoneNumber) }
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

    val accountInfoViewModel = AccountInfoViewModel()

    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        item {
            TextField(
                enabled = isUserInfoTextFieldEnabled,
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Nome") }
            )
            Spacer(modifier = Modifier.height(15.dp))
        }

        item {
            TextField(
                enabled = isUserInfoTextFieldEnabled,
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Cognome") }
            )
            Spacer(modifier = Modifier.height(15.dp))
        }

        item {
            TextField(
                enabled = false,
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") }
            )
            Spacer(modifier = Modifier.height(15.dp))
        }

        item {
            TextField(
                enabled = isUserInfoTextFieldEnabled,
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") }
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            TextField(
                enabled = isUserInfoTextFieldEnabled,
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Numero di telefono") }
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            TextField(
                value = password,
                onValueChange = { password = it },
                enabled = isPasswordTextFieldEnabled,
                label = { Text("Password") }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            Button(onClick = {
                if (isPasswordTextFieldEnabled) {
                    coroutineScope.launch {
                        val responseCode = accountInfoViewModel.changePassword(password)
                        if (responseCode == "success") {
                            password = ""
                            showPopupMessage = "Password modificata con successo"
                        } else {
                            showPopupMessage = "Errore durante la modifica della password"
                        }
                        showPopup = true
                    }
                }
                isPasswordTextFieldEnabled = !isPasswordTextFieldEnabled
            })
            {
                Text(text = if (isPasswordTextFieldEnabled) "Conferma password" else "Modifica password")
            }

            Spacer(modifier = Modifier.height(15.dp))

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

            Spacer(modifier = Modifier.height(15.dp))

            Row {
                Button(onClick = {
                    if (isUserInfoTextFieldEnabled) {
                        coroutineScope.launch {
                            val responseCode = accountInfoViewModel.modifyUser(
                                user.value.id,
                                firstName,
                                lastName,
                                username,
                                email,
                                phoneNumber
                            )
                            showPopupMessage = if (responseCode == "success") {
                                "Informazioni modificate con successo"
                            } else {
                                "Errore durante la modifica delle informazioni"
                            }
                            println(responseCode)
                            showPopup = true
                        }
                        isUserInfoTextFieldEnabled = !isUserInfoTextFieldEnabled
                    }
                    else {
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
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = houseNumber,
                        onValueChange = { houseNumber = it },
                        label = { Text("Numero Civico") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("Città") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = zipCode,
                        onValueChange = { zipCode = it },
                        label = { Text("CAP") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = province,
                        onValueChange = { province = it },
                        label = { Text("Provincia") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = region,
                        onValueChange = { region = it },
                        label = { Text("Regione") }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
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
