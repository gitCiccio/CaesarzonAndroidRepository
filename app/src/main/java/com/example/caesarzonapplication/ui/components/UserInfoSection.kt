package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.caesarzonapplication.model.User
import kotlinx.coroutines.flow.StateFlow

@Composable
fun UserInfoSection(userViewModel: StateFlow<User>) {
    var name by rememberSaveable { mutableStateOf(userViewModel.value.username) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var addresses by remember { mutableStateOf(listOf("")) }
    var selectedAddress by remember { mutableStateOf(addresses[0]) }
    var showAddAddressDialog by remember { mutableStateOf(false) }
    var showRemoveAddressDialog by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var addressDropdownExpanded by remember { mutableStateOf(false) }

    Column {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome") }
        )

        Spacer(modifier = Modifier.height(15.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

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
                    .fillMaxWidth()
                    .clickable { addressDropdownExpanded = true }
            )
            DropdownMenu(
                expanded = addressDropdownExpanded,
                onDismissRequest = { addressDropdownExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                addresses.forEach { address ->
                    DropdownMenuItem(
                        text = { Text(text = address) },
                        onClick = {
                            selectedAddress  = address
                            addressDropdownExpanded  = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        Row {
            Button(onClick = { showAddAddressDialog = true }, modifier = Modifier.weight(1f)) {
                Text(text = "Aggiungi indirizzo")
            }

            Spacer(modifier = Modifier.width(15.dp))

            Button(onClick = { showRemoveAddressDialog = true }, modifier = Modifier.weight(1f)) {
                Text(text = "Rimuovi indirizzo")
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                modifier = Modifier.width(220.dp),
                value = if (passwordVisible) TextFieldValue("password123") else TextFieldValue("********"),
                onValueChange = {},
                label = { Text("Password") },
                enabled = false
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { passwordVisible = !passwordVisible }) {
                Text(softWrap = false, text= if (passwordVisible) "Nascondi" else "Mostra")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { /* Logica per salvare le informazioni aggiornate */ }) {
            Text(text = "Modifica password")
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
                    var street by remember { mutableStateOf(TextFieldValue("")) }
                    var houseNumber by remember { mutableStateOf(TextFieldValue("")) }
                    var city by remember { mutableStateOf(TextFieldValue("")) }
                    var zipCode by remember { mutableStateOf(TextFieldValue("")) }
                    var province by remember { mutableStateOf(TextFieldValue("")) }
                    var region by remember { mutableStateOf(TextFieldValue("")) }

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
