package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.caesarzonapplication.model.dto.userDTOS.AddressDTO
import com.example.caesarzonapplication.model.dto.userDTOS.CityDataDTO
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AddressViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAddressInfoSection(addressViewModel: AddressViewModel) {

    val addresses by addressViewModel.addresses.collectAsState()
    var selectedAddress by rememberSaveable { mutableStateOf(addresses.getOrNull(0)) }
    var showAddAddressDialog by rememberSaveable { mutableStateOf(false) }
    var addressDropdownExpanded by rememberSaveable { mutableStateOf(false) }
    var cityDropdownExpanded by rememberSaveable { mutableStateOf(false) }

    val filteredCities by addressViewModel.cityData.observeAsState(emptyList())
    val cityDataDTO by addressViewModel.cityDataDTO.observeAsState(null)


    val cityFocusRequester = remember { FocusRequester() }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        item{
        Text("Gestione Indirizzi", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            TextField(
                value = selectedAddress?.let {
                    "${it.roadName} ${it.houseNumber} ${it.city.city}"
                } ?: "Nessun indirizzo disponibile",
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
                        text = { Text(text = "${address.roadName} ${address.houseNumber} ${address.city.city}") },
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
                enabled = addresses.isNotEmpty(),
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
            ) {
                Text(text = "Rimuovi indirizzo")
            }
        }

        if (showAddAddressDialog) {
            var street by rememberSaveable { mutableStateOf("") }
            var houseNumber by rememberSaveable { mutableStateOf("") }
            var roadType by rememberSaveable { mutableStateOf("") }
            var city by rememberSaveable { mutableStateOf("") }
            var zipCode by rememberSaveable { mutableStateOf("") }
            var province by rememberSaveable { mutableStateOf("") }
            var region by rememberSaveable { mutableStateOf("") }

            val isFormValid = street.isNotBlank() && houseNumber.isNotBlank() && roadType.isNotBlank() &&
                    city.isNotBlank() && zipCode.isNotBlank() && province.isNotBlank() && region.isNotBlank()

            LaunchedEffect(cityDataDTO) {
                cityDataDTO?.let { dto ->
                    zipCode = dto.cap ?: "Cap non disponibile"
                    province = dto.province ?: "Provincia non disponibile"
                    region = dto.region ?: "Regione non disponibile"
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = MaterialTheme.shapes.medium)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Aggiungi Indirizzo",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
                OutlinedTextField(
                    value = street,
                    onValueChange = { street = it },
                    label = { Text("Via") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = roadType,
                    onValueChange = { roadType = it },
                    label = { Text("Tipologia via") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = houseNumber,
                    onValueChange = { houseNumber = it },
                    label = { Text("Numero civico") },
                    singleLine = true,
                )

                ExposedDropdownMenuBox(
                    expanded = cityDropdownExpanded,
                    onExpandedChange = {
                        if (filteredCities.isNotEmpty()) {
                            cityDropdownExpanded = it
                        }
                        else {
                            cityFocusRequester.requestFocus()
                        }
                    }
                ){
                    OutlinedTextField(
                        value = city,
                        onValueChange = { input ->
                            city = input
                            addressViewModel.getCityTip(input)
                            cityDropdownExpanded = true
                        },
                        label = { Text("Inserisci città") },
                        singleLine = true,
                        modifier = Modifier
                            .menuAnchor()
                            .focusRequester(cityFocusRequester),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cityDropdownExpanded) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = cityDropdownExpanded,
                        onDismissRequest = { cityDropdownExpanded = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .background(Color.White, shape = MaterialTheme.shapes.medium))
                    {
                        filteredCities.forEach { cityData ->
                            DropdownMenuItem(
                                text = { Text(text = cityData) },
                                onClick = {
                                    city = cityData
                                    addressViewModel.getFullCityData(city)
                                    if (cityDataDTO != null) {
                                        zipCode = cityDataDTO?.cap ?: "Cap non disponibile"
                                        province = cityDataDTO?.province ?: "Provincia non disponibile"
                                        region = cityDataDTO?.region ?: "Regione non disponibile"
                                    }
                                    cityDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = zipCode,
                    onValueChange = { zipCode = it },
                    label = { Text("CAP") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = province,
                    onValueChange = { province = it },
                    label = { Text("Provincia") },
                    singleLine = true,
                )
                OutlinedTextField(
                    value = region,
                    onValueChange = { region = it },
                    label = { Text("Regione") },
                    singleLine = true,
                )
                Button(
                    onClick = {
                        if (isFormValid) {
                            val newAddressDTO = AddressDTO(
                                id = "",
                                roadName = street,
                                houseNumber = houseNumber,
                                roadType = roadType,
                                city = CityDataDTO(0, city, zipCode, province, region)
                            )
                            addressViewModel.addAddress(newAddressDTO)
                            showAddAddressDialog = false
                        }
                    },
                    enabled = isFormValid,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Salva")
                } }
            }
        }
    }
}