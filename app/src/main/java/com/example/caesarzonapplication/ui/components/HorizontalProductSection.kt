package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.R
import com.example.caesarzonapplication.model.dto.productDTOS.ProductSearchWithImage


@Composable
fun HorizontalProductSection(title: String, products: List<ProductSearchWithImage>, navController: NavHostController) {
    Column(modifier = Modifier
        .padding(16.dp)){
        Text(text=title, style= MaterialTheme.typography.titleLarge, color= MaterialTheme.colorScheme.primary)
        LazyRow {
            items(products){product->
                Card(
                    modifier = Modifier
                        .padding(10.dp)
                        .height(250.dp)
                        .width(150.dp)
                        .clickable { navController.navigate("productDetails/${product.product.productId}") },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(247, 177, 76, 255))
                    ) {

                        if (product.image != null) {
                            Image(
                                bitmap = product.image.asImageBitmap(),
                                contentDescription = product.product.productName,
                                modifier = Modifier
                                    .size(170.dp)
                                    .clip(MaterialTheme.shapes.medium)
                                    .align(Alignment.CenterHorizontally)
                            )
                        } else {
                            Image(
                                painter = painterResource(R.drawable.logo),
                                contentDescription = "immagine_prodotto_non_disponibile",
                                modifier = Modifier
                                    .size(170.dp)
                                    .clip(MaterialTheme.shapes.medium)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                        Text(
                            text = product.product.productName,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text =  (product.product.price.toString()+"€"),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )

                    }
                }
            }
        }
    }
}