package com.example.caesarzonapplication.ui.screens

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.model.viewmodels.ProductsViewModel
import com.example.caesarzonapplication.ui.components.ProductCard
import kotlinx.coroutines.delay

@Composable
fun ProductSearchResultsScreen(query: String, productsViewModel: ProductsViewModel, navController: NavHostController) {


    val productList by productsViewModel.productList.collectAsState(emptyList())

    LaunchedEffect(query) {
        if (isCategory(query)) {
            productsViewModel.getProductsByCategory(query)
        } else {
            productsViewModel.searchProducts(query)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        if (productsViewModel.isLoading.value) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize())
            }
        } else {
            if (productList.isEmpty()) {
                Image(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Nessun prodotto trovato",
                    modifier = Modifier
                        .padding(16.dp)
                )
                Text(
                    text = "Nessun prodotto trovato per \"$query\"",
                    modifier = Modifier
                        .padding(16.dp)
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3), // 3 prodotti per riga
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                ) {
                    items(productList) {product ->
                        ProductCard(product = product, navController = navController)
                    }
                }
            }
        }
    }
}


fun isCategory(query: String): Boolean {
    val categories= listOf(
        "Atletica", "Pallavolo", "Basket", "Tennis", "Nuoto",
        "Calcio", "Arti Marziali", "Ciclismo", "Sci"
    )
    return categories.contains(query.lowercase()) || categories.contains(query.uppercase()) || categories.contains(query)
}

