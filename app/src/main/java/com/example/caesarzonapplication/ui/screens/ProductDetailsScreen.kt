package com.example.caesarzonapplication.ui.screens

import ProductActions
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.R
import com.example.caesarzonapplication.model.Product
import com.example.caesarzonapplication.ui.components.AppTopBar
import com.example.caesarzonapplication.ui.components.ProductDetails
import com.example.caesarzonapplication.ui.components.ProductReviews
import com.example.caesarzonapplication.viewmodels.AdminViewModels.AdminProductViewModel

@Composable
fun ProductDetailsScreen(query: String, navController: NavHostController) {
    // Supponendo che tu abbia un modo per ottenere il prodotto dal nome
    val sampleProduct = Product(
        name = query, // Usa il nome del prodotto dal parametro
        imageRes = R.drawable.logo, // Poi aggiungere per ricavarla dal db
        price = 29.99,
        description = "This is a sample product description.",
        isInTheShoppingCart = false,
        quantity = 1
    )

    Scaffold(
        topBar = { AppTopBar(navController) },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                item {
                    ProductDetails(sampleProduct)
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    ProductActions(AdminProductViewModel())
                }
                item{
                    Spacer(modifier = Modifier.height(24.dp))
                }
                item {
                    ProductReviews(navController)
                }
            }
        },
        bottomBar = {Spacer(modifier = Modifier.padding(60.dp)) }
    )
}
