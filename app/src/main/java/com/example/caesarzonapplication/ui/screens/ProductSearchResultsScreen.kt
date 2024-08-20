package com.example.caesarzonapplication.ui.screens

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.model.viewmodels.ProductsViewModel
import com.example.caesarzonapplication.ui.components.ProductCard

@Composable
fun ProductSearchResultsScreen(query: String, productsViewModel: ProductsViewModel, navController: NavHostController) {

    val isLoading by productsViewModel.isLoading

    // Crea una transizione per combinare più animazioni
    val transition = updateTransition(targetState = isLoading, label = "CombinedTransition")

    // Animazione di fading (opacità)
    val alpha by transition.animateFloat(label = "AlphaAnimation") { isLoading ->
        if (isLoading) 1f else 0f
    }

    // Animazione di slide (scorrimento laterale)
    val offsetX by transition.animateDp(label = "OffsetXAnimation") { isLoading ->
        if (isLoading) 300.dp else 0.dp
    }

    // Animazione di scaling (ingrandimento)
    val scale by transition.animateFloat(label = "ScaleAnimation") { isLoading ->
        if (isLoading) 0.8f else 1f
    }

    val productList = if (isCategory(query)) {
        productsViewModel.getProductsByCategory(query)
    } else {
        productsViewModel.searchProducts(query)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize()
                    .graphicsLayer(
                        alpha = alpha,
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offsetX.value
                    )
            )
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
                    modifier = Modifier.fillMaxSize().padding(4.dp)
                ) {
                    items(productList) {
                        for (product in productList) {
                            ProductCard(product, navController)
                        }
                    }
                }
            }
        }
    }
}


fun isCategory(query: String): Boolean {
    val categories= listOf(
        "Atletica", "Pallavolo", "Basket", "Tennis", "Nuoto",
        "Calcio", "Lotta", "Ciclismo", "Sciismo"
    )
    return categories.contains(query.lowercase()) || categories.contains(query.uppercase()) || categories.contains(query)
}

