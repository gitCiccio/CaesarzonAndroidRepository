package com.example.caesarzonapplication.ui.screens


import ProductActions
import android.widget.Button
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.R
import com.example.caesarzonapplication.model.Product
import com.example.caesarzonapplication.ui.components.AddReviewPopup
import com.example.caesarzonapplication.ui.components.AdminNavigationBottomBar
import com.example.caesarzonapplication.ui.components.AppTopBar
import com.example.caesarzonapplication.ui.components.ProductDetails
import com.example.caesarzonapplication.ui.components.ProductReviews

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

    Scaffold (
        topBar = { AppTopBar(navController) },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                item{
                    ProductDetails(sampleProduct)
                }
                item{
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item{
                    ProductActions()
                }
                item{
                    Spacer(modifier = Modifier.height(30.dp))
                }
                item{
                    ProductReviews(navController)
                }



            }
        },
        bottomBar = { AdminNavigationBottomBar(navController) }
    )
}
