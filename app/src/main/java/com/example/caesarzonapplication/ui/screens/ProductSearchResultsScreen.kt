package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.model.viewmodels.ProductsViewModel
import com.example.caesarzonapplication.ui.components.ProductCard
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProductSearchResultsScreen(query: String, productsViewModel: ProductsViewModel, navController: NavHostController) {

    val productList by productsViewModel.productList.collectAsState(emptyList())
    var priceRange by remember { mutableStateOf(0f..100f) }
    var isClothing by remember { mutableStateOf(true) }
    var searchJob: Job? by remember { mutableStateOf(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(query) {
        if (isCategory(query)) {
            productsViewModel.getProductsByCategory(query)
        } else {
            productsViewModel.searchProducts(query, priceRange.start.toDouble(), priceRange.endInclusive.toDouble(), isClothing)
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
                println("Errore nella ricerca")
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Range di prezzo: €${priceRange.start.toInt()} - €${priceRange.endInclusive.toInt()}")
                    Slider(
                        value = priceRange.start,
                        onValueChange = { newValue ->
                            priceRange = newValue..priceRange.endInclusive
                            searchJob?.cancel()

                            searchJob = coroutineScope.launch {
                                delay(500L)
                                productsViewModel.searchProducts(query, priceRange.start.toDouble(), priceRange.endInclusive.toDouble(), isClothing)
                            }
                        },
                        valueRange = 0f..100f,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    Text(text = if (isClothing) "Abbigliamento" else "Attrezzatura")
                    Switch(
                        checked = isClothing,
                        onCheckedChange = { isChecked ->
                            isClothing = isChecked
                            productsViewModel.searchProducts(query, priceRange.start.toDouble(), priceRange.endInclusive.toDouble(), isClothing)

                        }
                    )
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                ) {
                    items(productList) {product ->
                        ProductCard(
                            product = product.product,
                            product.image,
                            navController = navController)
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

