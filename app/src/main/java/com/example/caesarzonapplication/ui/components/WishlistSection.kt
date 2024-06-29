package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caesarzonapplication.model.dto.SingleWishlistProductDTO
import com.example.caesarzonapplication.viewmodels.WishlistViewModel
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun WishlistSection (visibility: Int, wishlistViewModel: WishlistViewModel) {

    var productMap by remember { mutableStateOf<Map<UUID, List<SingleWishlistProductDTO>>>(emptyMap()) }
    val coroutineScope = rememberCoroutineScope()
    var newWishlistName by remember { mutableStateOf("") }
    var wishlistIdProducts by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        wishlistViewModel.loadWishlists(visibility)
    }
    TextField(
        value = newWishlistName,
        onValueChange = { newWishlistName = it },
        label = { Text("Nome nuova lista") }
    )
    Button(modifier = Modifier.padding(8.dp),onClick = { wishlistViewModel.addWishlist(newWishlistName, visibility) }) {
        Text(text = "Aggiungi lista")
    }

    for (wishlist in wishlistViewModel.wishlists) {
        wishlistIdProducts = wishlist.id.toString()
        Column(modifier = Modifier.padding(10.dp)) {
            Spacer(modifier = Modifier.height(30.dp))
            Row { Text(text = wishlist.name) }
            Row {
                Button(modifier = Modifier.padding(8.dp),onClick = {
                    coroutineScope.launch {
                        val products = wishlistViewModel.getWishlistProducts(wishlist.id)
                        if (products != null) {
                            productMap = productMap + (wishlist.id to products)
                        }                    }
                })
                {
                    Text(text = "Visualizza")
                }
                Button(modifier = Modifier.padding(8.dp), onClick = { wishlistViewModel.emptyWishlist(wishlist.id)})
                {
                    Text(text = "Svuota")
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                }
                Button(modifier = Modifier.padding(8.dp),onClick = { wishlistViewModel.deleteWishlist(wishlist.id) })
                {
                    Text(text = "Elimina")
                    Icon(imageVector = Icons.Default.Close, contentDescription = null)
                }
            }
            productMap[wishlist.id]?.let { productList ->
                WishlistProductList(
                    productList,
                    wishlistViewModel,
                    wishlist.id
                )
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
            Spacer(modifier = Modifier.height(8.dp))
        }
    }

}

