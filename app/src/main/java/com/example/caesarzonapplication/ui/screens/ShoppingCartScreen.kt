package com.example.caesarzonapplication.ui.screens

import VerticalProductSection
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.caesarzonapplication.ui.components.EmptyShoppingCart
import com.example.caesarzonapplication.ui.components.HorizontalProductSection
import com.example.caesarzonapplication.ui.components.NavigationBottomBar
import com.example.caesarzonapplication.viewmodels.HomeViewModel
import com.example.caesarzonapplication.viewmodels.ProductsViewModel

@Composable
fun ShoppingCartScreen(padding: PaddingValues, shoppingCartViewModel: ProductsViewModel, homeViewModel: HomeViewModel) {
    Scaffold(
        topBar = {},
        bottomBar = { NavigationBottomBar(navController = rememberNavController()) },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(top = 20.dp)
                    .background(Color(247, 170, 76, 255)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                    if(shoppingCartViewModel.productInShoppingCart.isEmpty()){
                        EmptyShoppingCart()
                    }else{
                        VerticalProductSection(title = "I tuoi prodotti", products = shoppingCartViewModel.productInShoppingCart)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    HorizontalProductSection(title = "Altri prodotti",
                        products = homeViewModel.products)
            }
        }
    )
}
