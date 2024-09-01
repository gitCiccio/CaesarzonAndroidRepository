package com.example.caesarzonapplication.ui.screens.adminScreens

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.caesarzonapplication.model.dto.productDTOS.AvailabilitiesSingle
import com.example.caesarzonapplication.model.dto.productDTOS.SendAvailabilityDTO
import com.example.caesarzonapplication.model.dto.productDTOS.SendProductDTO
import com.example.caesarzonapplication.model.viewmodels.adminViewModels.AdminProductViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.ProductsViewModel
import com.example.caesarzonapplication.navigation.DetailsScreen
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(adminProductViewModel: AdminProductViewModel, productViewModel: ProductsViewModel, onChanges: Boolean) {

    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var imageBitmap by remember { mutableStateOf<androidx.compose.ui.graphics.ImageBitmap?>(null) }

    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            imageUri = uri

            uri?.let {
                val inputStream: InputStream? = context.contentResolver.openInputStream(it)
                inputStream?.let { stream ->
                    val bitmap = BitmapFactory.decodeStream(stream)
                    imageBitmap = bitmap.asImageBitmap()
                }
            }
        }
    )
    val navController = rememberNavController()

    var productName by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var brand by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf( "") }
    var discount by rememberSaveable { mutableStateOf("") }
    var primaryColor by rememberSaveable { mutableStateOf("") }
    var secondaryColor by rememberSaveable { mutableStateOf("") }
    var isClothing by rememberSaveable { mutableStateOf(false) }
    var availability by rememberSaveable { mutableStateOf(listOf<SendAvailabilityDTO>()) }
    var selectedSport by rememberSaveable { mutableStateOf("") }
    var selectedSize by rememberSaveable { mutableStateOf("") }
    var quantity by rememberSaveable { mutableStateOf("") }

    if(onChanges){
       productName = productViewModel.selectedProduct.value?.product?.name.toString()
       description = productViewModel.selectedProduct.value?.product?.description.toString()
        brand = productViewModel.selectedProduct.value?.product?.brand.toString()
        price = productViewModel.selectedProduct.value?.product?.price.toString()
        discount = productViewModel.selectedProduct.value?.product?.discount.toString()
        primaryColor = productViewModel.selectedProduct.value?.product?.primaryColor.toString()
        secondaryColor = productViewModel.selectedProduct.value?.product?.secondaryColor.toString()
        isClothing = productViewModel.selectedProduct.value?.product?.is_clothing ?: false


        productViewModel.selectedProduct.value?.image?.let { bitmap ->
            imageBitmap = bitmap.asImageBitmap()
        }

        availability = productViewModel.selectedProduct.value?.product?.availabilities?.map {
            SendAvailabilityDTO(
                amount = it.amount,
                size = it.size
            )
        } ?: listOf()


        selectedSport = productViewModel.selectedProduct.value?.product?.sport.toString()


    }


    var sportExpanded by remember { mutableStateOf(false) }
    var sizeExpanded by remember { mutableStateOf(false) }

    val sports = listOf(
        "Atletica", "Pallavolo", "Basket", "Tennis", "Nuoto",
        "Calcio", "Arti Marziali", "Ciclismo", "Sci"
    )

    val sizes = listOf("XS", "S", "M", "L", "XL")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = {
            item {
                Text(
                    text = "Aggiungi prodotto",
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp),
                    color = Color.Black
                )
            }
            item {
                OutlinedTextField(
                    value = productName,
                    onValueChange = { productName = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descrizione") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = brand,
                    onValueChange = { brand = it },
                    label = { Text("Brand") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Prezzo") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = discount,
                    onValueChange = { discount = it },
                    label = { Text("Sconto") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = primaryColor,
                    onValueChange = { primaryColor = it },
                    label = { Text("Colore primario") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = secondaryColor,
                    onValueChange = { secondaryColor = it },
                    label = { Text("Colore secondario") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                ExposedDropdownMenuBox(
                    expanded = sportExpanded,
                    onExpandedChange = { sportExpanded = !sportExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedSport,
                        onValueChange = { selectedSport = it },
                        label = { Text("Sport") },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        readOnly = true
                    )
                    ExposedDropdownMenu(
                        expanded = sportExpanded,
                        onDismissRequest = { sportExpanded = false }
                    ) {
                        sports.forEach { sport ->
                            DropdownMenuItem(
                                text = { Text(sport) },
                                onClick = {
                                    selectedSport = sport
                                    sportExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            item{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isClothing,
                        onClick = { isClothing = true }
                    )
                    Text(
                        text = "Abbigliamento",
                    )
                    RadioButton(
                        selected = !isClothing,
                        onClick = { isClothing = false }
                    )
                    Text(
                        text = "Attrezzatura",
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isClothing) {
                        ExposedDropdownMenuBox(
                            expanded = sizeExpanded,
                            onExpandedChange = { sizeExpanded = !sizeExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedSize,
                                onValueChange = { selectedSize = it },
                                label = { Text("Taglia") },
                                modifier = Modifier
                                    .menuAnchor()
                                    .width(100.dp), // Riduci la larghezza della TextField "Taglia"
                                readOnly = true
                            )
                            ExposedDropdownMenu(
                                expanded = sizeExpanded,
                                onDismissRequest = { sizeExpanded = false }
                            ) {
                                sizes.forEach { size ->
                                    DropdownMenuItem(
                                        text = { Text(size) },
                                        onClick = {
                                            selectedSize = size
                                            sizeExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = { Text("Quantità") },
                        modifier = Modifier.width(100.dp) // Imposta una larghezza simile per la TextField "Quantità"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if ((isClothing && selectedSize.isNotBlank() || !isClothing) && quantity.isNotBlank()) {
                                val qty = quantity.toIntOrNull() ?: 0
                                if (qty > 0) {
                                    availability = availability.toMutableList().apply {
                                        add(SendAvailabilityDTO(amount = qty, size = selectedSize))
                                    }
                                    if (isClothing) {
                                        selectedSize = ""
                                    }
                                    quantity = ""
                                }
                            }
                        }
                    ) {
                        Text("Aggiungi")
                    }
                }
            }
            items(availability.size) { availabilityIndex ->
                val availabilityItem = availability[availabilityIndex]
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if(isClothing){
                        Text(
                            text = "Taglia: ${availabilityItem.size}",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Text(
                        text = "Quantità: ${availabilityItem.amount}",
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                        availability = availability.toMutableList().apply {
                            removeAt(availabilityIndex)
                        }
                    }) {
                        if(!onChanges) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "Remove")
                        }
                    }
                }
            }
            item{
                if(onChanges){
                    Text("Disponibilità presenti")
                }
            }
            item {
                Button(onClick = {
                    imagePickerLauncher.launch("image/*")
                }) {
                    Text("Carica Immagine")
                }

                imageBitmap?.let { bitmap ->
                    Image(
                        bitmap = bitmap,
                        contentDescription = "Anteprima Immagine",
                        modifier = Modifier
                            .size(200.dp)
                            .padding(16.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            item {
                Button(
                    onClick = {
                        if (imageBitmap != null) {
                            adminProductViewModel.addProduct(
                                SendProductDTO(
                                    id = "",
                                    name = productName,
                                    description = description,
                                    brand = brand,
                                    price = price.toDouble(),
                                    discount = discount.toDouble(),
                                    primaryColor = primaryColor,
                                    secondaryColor = secondaryColor,
                                    is_clothing = isClothing,
                                    sport = selectedSport,
                                    availabilities = availability
                                ),
                                imageBitmap!!,
                                context,

                                onContinueShopping ={
                                    navController.navigate("home")
                                }
                            )
                        }
                    }
                ) {
                    Text(text = "Aggiungi prodotto")
                }

            }
        }
    )
}
