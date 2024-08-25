package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

    var addresses by rememberSaveable { mutableStateOf(listOf("Seleziona un indirizzo")) }
    var selectedAddress by rememberSaveable { mutableStateOf(addresses[0]) }
    var showAddAddressDialog by rememberSaveable { mutableStateOf(false) }
    var showRemoveAddressDialog by rememberSaveable { mutableStateOf(false) }
    var addressDropdownExpanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Gestione Indirizzi", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown Menu per selezionare l'indirizzo
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

        // Pulsanti per aggiungere e rimuovere indirizzi
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
                enabled = addresses.size > 1, // Non permettere la rimozione se c'è solo l'indirizzo placeholder
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            ) {
                Text(text = "Rimuovi indirizzo")
            }
        }

        // Dialogo per aggiungere un indirizzo
        if (showAddAddressDialog) {
            AddressInputDialog(
                onDismiss = { showAddAddressDialog = false },
                onSave = { newAddress ->
                    addresses = addresses + newAddress
                    selectedAddress = newAddress
                    showAddAddressDialog = false
                }
            )
        }

        // Dialogo per rimuovere un indirizzo
        if (showRemoveAddressDialog) {
            RemoveAddressDialog(
                addresses = addresses.filter { it != "Seleziona un indirizzo" },
                onDismiss = { showRemoveAddressDialog = false },
                onRemove = { addressToRemove ->
                    addresses = addresses - addressToRemove
                    selectedAddress = if (addresses.isNotEmpty()) addresses[0] else "Seleziona un indirizzo"
                    showRemoveAddressDialog = false
                }
            )
        }
    }
}

@Composable
fun AddressInputDialog(onDismiss: () -> Unit, onSave: (String) -> Unit) {
    var street by remember { mutableStateOf("") }
    var houseNumber by remember { mutableStateOf("") }
    var roadType by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var zipCode by remember { mutableStateOf("") }
    var province by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var isFormValid by remember { mutableStateOf(false) }

    LaunchedEffect(street, city) {
        isFormValid = street.isNotEmpty() && city.isNotEmpty()
    }

    Dialog(onDismissRequest = onDismiss) {
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
                Text(
                    text = "Aggiungi Indirizzo",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Form input fields
                TextField(
                    value = street,
                    onValueChange = { street = it },
                    label = { Text("Via") },
                    isError = street.isEmpty(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (street.isEmpty()) {
                    Text("La via è obbligatoria", color = Color.Red)
                }
                TextField(
                    value = roadType,
                    onValueChange = { roadType = it },
                    label = { Text("Tipologia via") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = houseNumber,
                    onValueChange = { houseNumber = it },
                    label = { Text("Numero Civico") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("Città") },
                    isError = city.isEmpty(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                if (city.isEmpty()) {
                    Text("La città è obbligatoria", color = Color.Red)
                }
                TextField(
                    value = zipCode,
                    onValueChange = { zipCode = it },
                    label = { Text("CAP") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = province,
                    onValueChange = { province = it },
                    label = { Text("Provincia") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = region,
                    onValueChange = { region = it },
                    label = { Text("Regione") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Save button
                Button(
                    onClick = {
                        if (isFormValid) {
                            val newAddress = "$street $houseNumber, $city, $zipCode, $province, $region"
                            onSave(newAddress)
                        }
                    },
                    enabled = isFormValid,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Salva")
                }
            }
        }
    }
}

@Composable
fun RemoveAddressDialog(addresses: List<String>, onDismiss: () -> Unit, onRemove: (String) -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
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
                Text(
                    text = "Rimuovi Indirizzo",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn {
                    items(addresses) { address ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Text(text = address, modifier = Modifier.weight(1f))
                            IconButton(onClick = { onRemove(address) }) {
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
