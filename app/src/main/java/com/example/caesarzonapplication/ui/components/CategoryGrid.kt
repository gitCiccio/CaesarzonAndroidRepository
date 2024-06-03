package com.example.caesarzonapplication.ui.components

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.Rgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import com.example.caesarzonapplication.R

@Composable
fun CategoryGrid(){
    val categories= listOf(
        "Atletica" to R.drawable.ic_atletica,
        "Pallavolo" to R.drawable.ic_pallavolo,
        "Basket" to R.drawable.ic_basket,
        "Tennis" to R.drawable.ic_tennis,
        "Nuoto" to R.drawable.ic_nuoto,
        "Calcio" to R.drawable.ic_calcio,
        "Lotta" to R.drawable.ic_lotta,
        "Ciclismo" to R.drawable.ic_ciclismo,
        "Sciismo" to R.drawable.ic_sciismo
    )
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(8.dp)
    ) {
        items(categories){ (category,iconRes) ->
            Button(
                onClick = {},
                modifier = Modifier
                    .padding(10.dp)
                    .aspectRatio(1f)
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = category,
                        modifier = Modifier.height(50.dp).padding(5.dp)
                    )
                    Text(text=category)
                }
            }
        }
    }
}