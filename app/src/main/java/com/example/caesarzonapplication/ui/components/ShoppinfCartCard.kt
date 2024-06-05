package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caesarzonapplication.R

@Preview
@Composable
fun ShoppinfCartCard() {
    var preferred by rememberSaveable { mutableStateOf(false) }
    var showMore by rememberSaveable { mutableStateOf(false) }
    var quantity by remember { mutableStateOf(1) }

    Card(modifier = Modifier
        .height(if (showMore) 700.dp else 500.dp)
        .width(400.dp)
    ){
        Image(painter = painterResource(id = R.drawable.mini), contentDescription = "foto_del_prodotto",
            Modifier
                .size(350.dp)
                .padding(10.dp))
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Text(text = "Nome del prodotto", fontSize = 30.sp,modifier = Modifier
                .padding(10.dp))
            IconButton(onClick = {preferred = !preferred}) {
                if (preferred)
                    Icon(imageVector = Icons.Filled.Favorite, contentDescription = "preferito")
                else
                    Icon(imageVector = Icons.Filled.FavoriteBorder, contentDescription = "non_preferito")
            }
        }


        Row (verticalAlignment = Alignment.CenterVertically){
            Button(onClick = {showMore = !showMore}, modifier = Modifier.padding(10.dp)) {
                Text(text = if (!showMore) "Mostra dettagli" else "Mostra meno")
            }
            Spacer(modifier = Modifier.width(30.dp))
            Row() {
                Button(onClick = {
                    if(quantity>0)
                        quantity-=1
                }) {
                    if (quantity>0) {
                        Text(text = "-")
                    }else{
                        Icon(imageVector = Icons.Filled.Close, contentDescription = null)
                    }
                }
                Box(modifier = Modifier
                    .height(60.dp)
                    .width(60.dp)
                    .background(Color.LightGray)) {
                    Text(modifier = Modifier.padding(20.dp), text = "${quantity}")
                }
                Button(onClick = {quantity+=1}) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                }
            }

        }
    }
}