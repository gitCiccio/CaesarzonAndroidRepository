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
fun WishlistSection(visibility: Int, wishlistViewModel: WishlistViewModel) {

    var selectedWishlistId by remember { mutableStateOf<UUID?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var newWishlistName by remember { mutableStateOf("") }
    var productList by remember { mutableStateOf<List<SingleWishlistProductDTO>?>(null) }

    LaunchedEffect(Unit) {
        wishlistViewModel.loadWishlists(visibility)
    }

    TextField(
        value = newWishlistName,
        onValueChange = { newWishlistName = it },
        label = { Text("Nome nuova lista") }
    )
    Button(modifier = Modifier.padding(8.dp), onClick = { wishlistViewModel.addWishlist(newWishlistName, visibility) }) {
        Text(text = "Aggiungi lista")
    }

    for (wishlist in wishlistViewModel.wishlists) {
        Column(modifier = Modifier.padding(10.dp)) {
            Spacer(modifier = Modifier.height(30.dp))
            Row { Text(text = wishlist.name) }
            Row {
                Button(modifier = Modifier.padding(8.dp), onClick = {
                    if (selectedWishlistId == wishlist.id) {
                        selectedWishlistId = null
                        productList = null
                    } else {
                        selectedWishlistId = wishlist.id
                        coroutineScope.launch {
                            productList = wishlistViewModel.getWishlistProducts(wishlist.id)
                        }
                    }
                }) {
                    Text(text = if (selectedWishlistId == wishlist.id) "Nascondi" else "Visualizza")
                }
                Button(
                    modifier = Modifier.padding(8.dp),
                    onClick = { wishlistViewModel.emptyWishlist(wishlist.id) }) {
                    Text(text = "Svuota")
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                }
                Button(
                    modifier = Modifier.padding(8.dp),
                    onClick = { wishlistViewModel.deleteWishlist(wishlist.id) }) {
                    Text(text = "Elimina")
                    Icon(imageVector = Icons.Default.Close, contentDescription = null)
                }
            }
            LaunchedEffect(Unit) {
                productList ?: wishlistViewModel.getWishlistProducts(wishlist.id)
            }
            if (selectedWishlistId == wishlist.id && productList != null) {
                WishlistProductList(
                    productList,
                    wishlistViewModel,
                    wishlist.id
                )
            }
            when (visibility) {
                0 ->Row{
                    Button(modifier = Modifier.padding(10.dp), onClick = { wishlistViewModel.changeWishlistVisibility(wishlist.id, 1) }) { Text(text = "Rendi condivisa") }
                    Button(modifier = Modifier.padding(10.dp), onClick = { wishlistViewModel.changeWishlistVisibility(wishlist.id, 0) }) { Text(text = "Rendi privata") }
                }
                1 -> Row{
                    Button(modifier = Modifier.padding(10.dp), onClick = { wishlistViewModel.changeWishlistVisibility(wishlist.id, 2) }) { Text(text = "Rendi condivisa") }
                    Button(modifier = Modifier.padding(10.dp), onClick = { wishlistViewModel.changeWishlistVisibility(wishlist.id, 0) }) { Text(text = "Rendi pubblica") }
                }
                2 -> Row{
                    Button(modifier = Modifier.padding(10.dp), onClick = { wishlistViewModel.changeWishlistVisibility(wishlist.id, 0) }) { Text(text = "Rendi pubblica") }
                    Button(modifier = Modifier.padding(10.dp), onClick = { wishlistViewModel.changeWishlistVisibility(wishlist.id, 2) }) { Text(text = "Rendi privata") }
                }
            }
        }
    }
}
