package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caesarzonapplication.R
import com.example.caesarzonapplication.model.Product
import retrofit2.http.Query

@Composable
fun ProductDetailsScreen(query: String) {
    // Supponendo che tu abbia un modo per ottenere il prodotto dal nome
    val sampleProduct = Product(
        name = query, // Usa il nome del prodotto dal parametro
        imageRes = R.drawable.logo, // Poi aggiungere per ricavarla dal db
        price = 29.99,
        description = "This is a sample product description.",
        isInTheShoppingCart = false,
        quantity = 1
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
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
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (sampleProduct.isInTheShoppingCart) {
            Text(
                text = "This product is in your shopping cart.",
                color = Color.Green,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            Button(
                onClick = { /* Logic to add to cart */ },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Add to Cart")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Button(onClick = { if (sampleProduct.quantity > 1) sampleProduct.quantity-- }) {
                Text(text = "-")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = sampleProduct.quantity.toString(), style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { sampleProduct.quantity++ }) {
                Text(text = "+")
            }
        }
    }
}
