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
import com.example.caesarzonapplication.model.dto.AddressDTO
import com.example.caesarzonapplication.model.dto.CityDataDTO
import com.example.caesarzonapplication.model.entities.userEntity.Address
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AccountInfoViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AddressViewModel

@Composable
fun UserAddressInfoSection(accountInfoViewModel: AccountInfoViewModel, addressViewModel: AddressViewModel) {

    var addresses = addressViewModel.addresses
    var selectedAddress by rememberSaveable { mutableStateOf(addresses.getOrNull(0)) }
    var showAddAddressDialog by rememberSaveable { mutableStateOf(false) }
    var addressDropdownExpanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Gestione Indirizzi", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            TextField(
                value = (selectedAddress?.roadName + " " + selectedAddress?.houseNumber + " " + selectedAddress?.city?.city).takeIf { it.isNotBlank() } ?: "Nessun indirizzo disponibile",
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
                        text = { Text(text = address.roadName+" "+address.houseNumber+" "+address.city.city) },
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
                onClick = { selectedAddress?.let { addressViewModel.deleteAddress(it) } },
                enabled = addresses.size > 0,
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            ) {
                Text(text = "Rimuovi indirizzo")
            }
        }

        // Dialogo per aggiungere un indirizzo
        if (showAddAddressDialog) {
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

            Dialog(onDismissRequest = {showAddAddressDialog = false} ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = MaterialTheme.shapes.medium)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item{
                        Text(
                            text = "Aggiungi Indirizzo",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                    item{
                        TextField(
                            value = street,
                            onValueChange = { street = it },
                            label = { Text("Via") },
                            singleLine = true,
                        )
                    }
                    item{
                        TextField(
                            value = roadType,
                            onValueChange = { roadType = it },
                            label = { Text("Tipologia via") },
                            singleLine = true,
                        )
                    }
                    item{
                        TextField(
                            value = houseNumber,
                            onValueChange = { houseNumber = it },
                            label = { Text("Numero civico") },
                            singleLine = true,
                        )
                    }
                    item{
                        var filteredCities by remember { mutableStateOf(emptyList<CityDataDTO>()) }
                        OutlinedTextField(
                            value = city,
                            onValueChange = { input ->
                                city = input
                                filteredCities = if (input.isNotEmpty()) {
                                    addressViewModel.cityData.filter { it.city.contains(input, ignoreCase = true) }
                                } else {
                                    emptyList()
                                }
                                addressDropdownExpanded = filteredCities.isNotEmpty()
                            },
                            label = { Text("Inserisci città") },
                            singleLine = true
                        )
                        DropdownMenu(
                            expanded = addressDropdownExpanded,
                            onDismissRequest = { addressDropdownExpanded = false },
                        ) {
                            filteredCities.forEach { cityData ->
                                DropdownMenuItem(
                                    text = { Text(text = cityData.city) },
                                    onClick = {
                                        city = cityData.city
                                        addressDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    item{
                        TextField(
                            value = zipCode,
                            onValueChange = { zipCode = it },
                            label = { Text("CAP") },
                            singleLine = true,
                        )
                    }
                    item{
                        TextField(
                            value = province,
                            onValueChange = { province = it },
                            label = { Text("Provincia") },
                            singleLine = true,
                        )
                    }
                    item{
                        TextField(
                            value = region,
                            onValueChange = { region = it },
                            label = { Text("Regione") },
                            singleLine = true,
                        )
                    }
                    item{
                        Button(
                            onClick = {
                                if (isFormValid) {
                                    val newAddress = AddressDTO("", street, houseNumber, "", CityDataDTO("", city, zipCode, region, province))
                                    addresses.add(newAddress)
                                    selectedAddress = newAddress
                                    val newCityDataDTO = CityDataDTO("", city, zipCode, region, province)
                                    val newAddressDTO = AddressDTO("", street, houseNumber, "", newCityDataDTO)
                                    showAddAddressDialog = false
                                    addressViewModel.addAddress(newAddressDTO)
                                }
                            },
                            enabled = isFormValid,
                            modifier = Modifier.padding(top = 16.dp),
                            content = {Text(text = "Salva")}
                        )
                    }
                }
            }
        }
    }
}
