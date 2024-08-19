package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.model.dto.ProductSearchDTO
import com.example.caesarzonapplication.ui.components.HorizontalProductSection
import com.example.caesarzonapplication.model.viewmodels.ProductsViewModel

@Composable
fun ProductSearchResultsScreen(query: String, productsViewModel: ProductsViewModel, navController: NavHostController){
    //val filteredResults = productsViewModel.loadSimpleProducts()
        //.filter{ it.name.contains(query, ignoreCase = true) }
    var productList = mutableListOf<ProductSearchDTO>()

    Column(
        modifier = Modifier
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


