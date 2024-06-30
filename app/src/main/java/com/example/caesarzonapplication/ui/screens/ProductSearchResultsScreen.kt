package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.caesarzonapplication.model.Product
import com.example.caesarzonapplication.model.dto.ProductDTO
import com.example.caesarzonapplication.model.dto.ProductSearchDTO
import com.example.caesarzonapplication.ui.components.AppTopBar
import com.example.caesarzonapplication.ui.components.HorizontalProductSection
import com.example.caesarzonapplication.ui.components.NavigationBottomBar
import com.example.caesarzonapplication.viewmodels.ProductsViewModel

@Composable
fun ProductSearchResultScreen(query: String, productsViewModel: ProductsViewModel, navController: NavHostController){
    val filteredResults = productsViewModel.loadSimpleProducts()
        //.filter{ it.name.contains(query, ignoreCase = true) }
    var productList = mutableListOf<ProductSearchDTO>()

    Scaffold (
        topBar = { AppTopBar(navController)},
        bottomBar = { Spacer(modifier = Modifier.padding(60.dp))},
        content = { padding ->
            Column( modifier = Modifier
                .padding(padding)
                .background(Color.White)
        ) {
            if (productList.isEmpty()) {
                Text(
                    text = "Nessun prodotto trovato per \"$query\"",
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    items(productList.size) { product ->
                        HorizontalProductSection("", productList, navController)
                    }
                }
            }
        }
        }
    )
}

