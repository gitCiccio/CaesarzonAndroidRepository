package com.example.caesarzonapplication.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.model.dto.ProductDTO
import com.example.caesarzonapplication.model.dto.ProductSearchDTO

@Composable
fun ProductCard(product: ProductSearchDTO, image: Bitmap?, navController: NavHostController){
    Card(
        modifier = Modifier
            .padding(10.dp)
            .height(200.dp)
            .width(150.dp)
            .clickable { navController.navigate("productDetails/${product.productId}") },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(247, 177, 76, 255))
        ) {
            image?.asImageBitmap()?.let {
                Image(
                    bitmap = it,
                    contentDescription = product.productName,
                    modifier = Modifier
                        .height(100.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Text(
                text = product.productName,
                style = MaterialTheme.typography.bodyMedium,
                //modifier = Modifier.padding(8.dp)
            )
            Text(
                text =  (product.price.toString()+"€"),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                //modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
            Text(
                text = "Inserire la descrizione del prodotto",
                style = MaterialTheme.typography.bodySmall,
                //modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}