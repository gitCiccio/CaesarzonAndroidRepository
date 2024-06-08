package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.caesarzonapplication.model.Product


@Composable
fun HorizontalProductSection(title: String, products: List<Product>) {
    Column(modifier = Modifier.padding(16.dp)){
        Text(text=title, style= MaterialTheme.typography.titleLarge, color= MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {
            items(products){product->
                ProductCard(product = product, navHostController = rememberNavController())
            }
        }
    }
}