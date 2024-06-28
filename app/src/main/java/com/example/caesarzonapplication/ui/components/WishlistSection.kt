package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caesarzonapplication.model.dto.SingleWishlistProductDTO
import com.example.caesarzonapplication.viewmodels.WishlistViewModel

@Composable
fun WishlistSection (visibility: Int, wishlistViewModel: WishlistViewModel) {

    var productList by remember { mutableStateOf<List<SingleWishlistProductDTO>?>(null) }

    LaunchedEffect(Unit) {
        wishlistViewModel.loadWishlists(visibility)
    }

    LazyColumn {
        items(wishlistViewModel.wishlists) { wishlist ->
            Column(modifier = Modifier.padding(10.dp)) {
                Row {
                    Text(text = wishlist.name)
                    Button(onClick = {
                            productList = wishlistViewModel.getWishlistProducts(wishlist.id)
                        })
                    {
                        Text(text = "Visualizza")
                    }
                    Button(onClick = { /* Elimina lista */ })
                    {
                        Icon(imageVector = Icons.Default.Close, contentDescription = null)
                    }
                    Button(onClick = { /* Svuota lista */ })
                    {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }
                    if (visibility == 0) {
                        Button(onClick = { /*Rendi privata*/ }) {
                            Text(text = "Rendi privata")
                        }
                    } else if (visibility == 2) {
                        Button(onClick = { /*Rendi pubblica*/ }) {
                            Text(text = "Rendi pubblica")
                        }
                    } else if (visibility == 1) {
                        Button(onClick = { /*Rendi pubblica*/ }) {
                            Text(text = "Rendi pubblica")
                        }
                    }
                }
                productList?.let { productList -> WishlistProductList(productList) }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}