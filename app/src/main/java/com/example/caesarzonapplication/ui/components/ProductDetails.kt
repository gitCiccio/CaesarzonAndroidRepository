package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caesarzonapplication.model.Product


@Composable
fun ProductDetails(product : Product){
    Column(modifier = Modifier .fillMaxWidth()) {
        Text(text = "Categoria: Esempio"  ,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.align(Alignment.Start)
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = product.name,
            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp),
            modifier = Modifier .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = product.imageRes),
            contentDescription = product.name,
            modifier = Modifier
                .size(200.dp)
                .clip(MaterialTheme.shapes.medium)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))


        Text(
            text = "Price: \$${product.price}",
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp),
            color = Color.Gray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))


        Text(
            text = "Descrizione: ",
            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 22.sp),
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = product.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .align(Alignment.Start)
                .fillMaxWidth(0.9f)
        )
    }
}