package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.caesarzonapplication.R
import com.example.caesarzonapplication.ui.components.AppTopBar
import com.example.caesarzonapplication.ui.components.NavigationBottomBar

@Composable
fun HomeScreen(paddingValues: PaddingValues){
    Scaffold(
        topBar = { AppTopBar() },
        bottomBar = { NavigationBottomBar(navController = rememberNavController())},
        content = { padding ->
            Column (modifier = Modifier.padding(padding)){
                ProductSection(title ="Offerte speciali", products = sampleProducts)
                Spacer(modifier = Modifier.height(16.dp))
                ProductSection(title = "Novità", products = sampleProducts)
            }
        }
    )
}

@Composable
fun ProductSection(title: String, products: List<com.example.caesarzonapplication.ui.screens.Product>) {
    Column(modifier = Modifier.padding(16.dp)){
        Text(text=title, style=MaterialTheme.typography.titleLarge, color=MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {
            items(products){product->
                ProductCard(product =product)
            }
        }
    }
}

@Composable
fun ProductCard(product: com.example.caesarzonapplication.ui.screens.Product) {
    Card (modifier = Modifier
        .padding(end = 8.dp)
        .width(150.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ){
        Column {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "\$${product.price}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

data class Product(val name: String, val imageRes: Int, val price: Double)

val sampleProducts = listOf(
    Product("Product 1", R.drawable.logo, 29.99),
    Product("Product 2", R.drawable.logo, 59.99),
    Product("Product 3", R.drawable.logo, 19.99),
    Product("Product 4", R.drawable.logo, 99.99)
)