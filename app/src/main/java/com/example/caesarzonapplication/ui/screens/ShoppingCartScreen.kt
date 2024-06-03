package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.caesarzonapplication.ui.components.EmptyShoppingCart
import com.example.caesarzonapplication.ui.components.HorizontalProductSection
import com.example.caesarzonapplication.ui.components.NavigationBottomBar
import com.example.caesarzonapplication.ui.components.VerticalProductSection
import com.example.caesarzonapplication.viewmodels.ProductsViewModel

@Composable
fun ShoppingCartScreen(padding: PaddingValues, shoppingCartViewModel: ProductsViewModel) {
    Scaffold(
        topBar = {},
        bottomBar = { NavigationBottomBar(navController = rememberNavController())},
        content = { padding ->
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(top = 20.dp)
                    .background(Color(247, 170, 76, 255))
            ) {
                if(shoppingCartViewModel.productInShoppingCart.isEmpty()){
                    Column(modifier = Modifier
                        .padding(100.dp)) {
                        EmptyShoppingCart()
                    }
                }else {
                    Column(modifier = Modifier
                        .padding(100.dp)){
                        VerticalProductSection(title = "Prodotti", products = shoppingCartViewModel.productInShoppingCart)
                    }
                }
                HorizontalProductSection(title = "Altri prodotti", products = shoppingCartViewModel.productInShoppingCart)
            }
        }

    )
}