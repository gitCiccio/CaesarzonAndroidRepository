package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AccountInfoViewModel

@Composable
fun UserAddressInfoSection(accountInfoViewModel: AccountInfoViewModel) {

    var addresses by remember { mutableStateOf(listOf("Seleziona un indirizzo")) }
    var selectedAddress by rememberSaveable { mutableStateOf(addresses[0]) }
    var showAddAddressDialog by rememberSaveable { mutableStateOf(false) }
    var showRemoveAddressDialog by rememberSaveable { mutableStateOf(false) }
    var addressDropdownExpanded by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                TextField(
                    value = selectedAddress,
                    onValueChange = {},
                    label = { Text("Indirizzo di spedizione") },
                    enabled = false,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Drop-down arrow",
                            tint = if (addressDropdownExpanded) MaterialTheme.colorScheme.primary else Color.Gray,
                            modifier = Modifier.clickable { addressDropdownExpanded = !addressDropdownExpanded }
                        )
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
                                selectedAddress = address
                                addressDropdownExpanded = false
                            }
                        )
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { showAddAddressDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                ) {
                    Text(text = "Aggiungi indirizzo")
                }
                Button(
                    onClick = { showRemoveAddressDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                ) {
                    Text(text = "Rimuovi indirizzo")
                }
            }
        }

        if (showAddAddressDialog) {
            item {
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
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    Icons.Filled.Close,
                                    contentDescription = "Chiudi",
                                    modifier = Modifier
                                        .clickable { showAddAddressDialog = false }
                                )
                            }

                            var street by remember { mutableStateOf("") }
                            var houseNumber by remember { mutableStateOf("") }
                            var city by remember { mutableStateOf("") }
                            var zipCode by remember { mutableStateOf("") }
                            var province by remember { mutableStateOf("") }
                            var region by remember { mutableStateOf("") }
                            var streetError by remember { mutableStateOf("") }
                            var cityError by remember { mutableStateOf("") }

                            TextField(
                                value = street,
                                onValueChange = {
                                    street = it
                                    streetError = if (it.isEmpty()) "La via è obbligatoria" else ""
                                },
                                label = { Text("Via") },
                                isError = streetError.isNotEmpty()
                            )
                            if (streetError.isNotEmpty()) {
                                Text(streetError, color = Color.Red)
                            }

                            TextField(
                                value = houseNumber,
                                onValueChange = { houseNumber = it },
                                label = { Text("Numero Civico") }
                            )
                            TextField(
                                value = city,
                                onValueChange = {
                                    city = it
                                    cityError = if (it.isEmpty()) "La città è obbligatoria" else ""
                                },
                                label = { Text("Città") },
                                isError = cityError.isNotEmpty()
                            )
                            if (cityError.isNotEmpty()) {
                                Text(cityError, color = Color.Red)
                            }
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
                            Button(
                                onClick = {
                                    if (street.isNotEmpty() && city.isNotEmpty()) {
                                        val newAddress =
                                            "$street $houseNumber, $city, $zipCode, $province, $region"
                                        addresses = addresses + newAddress
                                        selectedAddress = newAddress
                                        showAddAddressDialog = false
                                    }
                                },
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                Text(text = "Salva")
                            }
                        }
                    }
                }
            }
        }

        if (showRemoveAddressDialog) {
            item {
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
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    Icons.Filled.Close,
                                    contentDescription = "Chiudi",
                                    modifier = Modifier
                                        .clickable { showRemoveAddressDialog = false }
                                )
                            }
                            addresses.filter { it != "Seleziona un indirizzo" }.forEach { address ->
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
                                            addresses = listOf("Seleziona un indirizzo")
                                            selectedAddress = addresses[0]
                                        }
                                        showRemoveAddressDialog = false
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
    }
}
