package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.R
import com.example.caesarzonapplication.model.Product

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

    // Lista fittizia di recensioni

    // Stato per tenere traccia dello stato di espansione delle recensioni
    var isReviewsExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Category section
        Text(
            text = "Category: Sample Category", // Replace with actual category
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Product name
        Text(
            text = sampleProduct.name,
            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = sampleProduct.imageRes),
            contentDescription = sampleProduct.name,
            modifier = Modifier
                .size(200.dp)
                .clip(MaterialTheme.shapes.medium)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Price: \$${sampleProduct.price}",
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp),
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Description",
            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 22.sp),
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = sampleProduct.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.Start)
                .fillMaxWidth(0.9f)
        )

        Spacer(modifier = Modifier.height(50.dp))

        var isReviewsExpanded by remember { mutableStateOf(false) }

// Sezione recensioni
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isReviewsExpanded = !isReviewsExpanded },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Recensioni",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Icon(
                imageVector = if (isReviewsExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = "Espandi/Comprimi Recensioni",
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        /*if (isReviewsExpanded) {
            // Se le recensioni sono espandibili, mostra le recensioni
            Column {
                reviews.forEach { review ->
                    Text(
                        text = "${review.name}: ${review.surname}: ${review.comment}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                    )
                }
            }
        }*/
    }
}
