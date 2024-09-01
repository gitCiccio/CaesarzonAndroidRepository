import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.model.dto.productDTOS.ProductDTO
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.isAdmin
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.logged
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.ShoppingCartViewModel
import com.example.caesarzonapplication.ui.components.GenericMessagePopup
import com.example.caesarzonapplication.ui.components.WishlistPopup
import com.example.caesarzonapplication.model.viewmodels.adminViewModels.AdminProductViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.WishlistViewModel
import com.example.caesarzonapplication.navigation.AdminBottomBarScreen
import com.example.caesarzonapplication.navigation.DetailsScreen
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProductActions(
    navController: NavHostController,
    adminProductViewModel: AdminProductViewModel,
    shoppingCartViewModel: ShoppingCartViewModel,
    wishlistViewModel: WishlistViewModel,
    productDTO: ProductDTO
) {
    var showPopup by rememberSaveable { mutableStateOf(false) }
    var showWishlistPopup by rememberSaveable { mutableStateOf(false) }
    var selectedSize by remember { mutableStateOf("M") }
    var quantity by remember { mutableStateOf("1") }
    var expanded by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    if (!isAdmin.value) {
        if (showPopup) {
            GenericMessagePopup(
                message = "Prodotto aggiunto al carrello con successo!",
                onDismiss = { showPopup = false }
            )
        }

        if (showWishlistPopup) {
            WishlistPopup(
                wishlistViewModel = wishlistViewModel,
                productId = productDTO.id,
                onDismiss = { showWishlistPopup = false }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(productDTO.is_clothing){
                    Text(
                        text = "Taglia:",
                        modifier = Modifier.padding(end = 10.dp),
                        style = TextStyle(fontSize = 16.sp)
                    )
                    Box {
                        Button(
                            onClick = { expanded = !expanded },
                            modifier = Modifier.padding(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500))
                        ) {
                            Text(selectedSize, style = TextStyle(fontSize = 14.sp, color = Color.White))
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            listOf("XS", "S", "M", "L", "XL").forEach { size ->
                                DropdownMenuItem(
                                    text = { Text(text = size) },
                                    onClick = {
                                        selectedSize = size
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Quantità:",
                    modifier = Modifier.padding(end = 10.dp),
                    style = TextStyle(fontSize = 16.sp)
                )
                TextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    modifier = Modifier
                        .width(80.dp)
                        .padding(10.dp),
                    textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        if(productDTO.is_clothing){
                            shoppingCartViewModel.addProductCart(productDTO.id, size = selectedSize, quantity.toInt())
                            shoppingCartViewModel.checkAvailability()
                            coroutineScope.launch {
                                delay(1000)
                                navController.navigate(DetailsScreen.CheckOutScreen.route)
                            }
                        }else{
                            shoppingCartViewModel.addProductCart(productDTO.id, null, quantity.toInt())
                            shoppingCartViewModel.checkAvailability()
                            coroutineScope.launch {
                                delay(1000)
                                navController.navigate(DetailsScreen.CheckOutScreen.route)
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(72.dp)
                        .padding(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500)),
                    enabled = logged.value
                ) {
                    Text(
                        "Acquista subito",
                        style = TextStyle(fontSize = 14.sp, color = Color.White),
                        textAlign = TextAlign.Center
                    )
                }
                Button(
                    onClick = {
                        showPopup = true
                        if(productDTO.is_clothing){
                            shoppingCartViewModel.addProductCart(productDTO.id, size = selectedSize, quantity.toInt())
                        }else{
                            shoppingCartViewModel.addProductCart(productDTO.id, null, quantity.toInt())
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(72.dp)
                        .padding(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500)),
                    enabled = logged.value
                ) {
                    Text(
                        "Aggiungi al carrello",
                        style = TextStyle(fontSize = 14.sp, color = Color.White),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Button(
                onClick = {
                    showWishlistPopup = true
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(48.dp)
                    .fillMaxWidth(0.5f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(103, 58, 183, 255)),
                enabled = logged.value
            ) {
                Text(
                    "Aggiungi alla lista desideri",
                    style = TextStyle(fontSize = 14.sp, color = Color.White),
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
        Row {
            Button(onClick = {
                navController.navigate(AdminBottomBarScreen.AddProduct.route+"/${false}")
            }) {
                Text(text = "Modifica")
            }
            Button(onClick = { adminProductViewModel.deleteProduct(productDTO.id) }) {
                Text(text = "Elimina")
            }
        }
    }
}
