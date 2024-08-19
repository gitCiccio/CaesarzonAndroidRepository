package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.model.viewmodels.ProductsViewModel
import com.example.caesarzonapplication.model.viewmodels.ShoppingCartViewModel
import com.example.caesarzonapplication.ui.components.EmptyShoppingCart
import com.example.caesarzonapplication.ui.components.HorizontalProductSection
import com.example.caesarzonapplication.ui.components.ShoppingCartCard

@Composable
fun ShoppingCartScreen(navController: NavHostController, logged: MutableState<Boolean>, productsViewModel: ProductsViewModel) {

    val shoppingCartViewModel = ShoppingCartViewModel()
    var showLoginDialog by rememberSaveable { mutableStateOf(false) }
    val newProducts = productsViewModel.newProducts
    val buyLaterProducts = shoppingCartViewModel.buyLaterProducts

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (shoppingCartViewModel.productInShoppingCart.isEmpty()) {
            item {
                EmptyShoppingCart()
            }
        } else {
            items(shoppingCartViewModel.productInShoppingCart) { it ->
                ShoppingCartCard(it, shoppingCartViewModel, navController)
                Spacer(modifier = Modifier.height(30.dp))
            }
            item {
                Row {
                    Button(onClick = { navController.navigate("home") },
                        modifier = Modifier
                            .padding(15.dp)
                            .height(60.dp)
                            .wrapContentWidth(Alignment.Start),
                        colors = buttonColors(
                            containerColor = Color.Gray
                        )
                    )
                    {
                        Text(text = "Continua a comprare",
                            style = TextStyle(fontSize = 16.sp),
                            softWrap = false
                        )
                    }
                    Button(onClick = { if(!logged.value){showLoginDialog = true} },
                        modifier = Modifier
                            .padding(15.dp)
                            .weight(1f)
                            .height(60.dp),
                        colors = buttonColors(
                            containerColor = Color.Green
                        ))
                    {
                        Text(text = "Procedi",
                            style = TextStyle(fontSize = 20.sp)
                        )
                    }
                }
            }
        }
        if(shoppingCartViewModel.buyLaterProducts.isNotEmpty()) {
            item {
                HorizontalProductSection(title = "Prodotti da comprare più tardi", buyLaterProducts, navController)
            }
        }
        item {
            HorizontalProductSection(title = "Altri prodotti", newProducts, navController)
        }
    }
}

