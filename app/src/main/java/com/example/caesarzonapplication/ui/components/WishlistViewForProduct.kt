package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caesarzonapplication.model.dto.WishlistDTO
import com.example.caesarzonapplication.viewmodels.WishlistViewModel

@Composable
fun WishlistViewForProduct(wishlistViewModel: WishlistViewModel, visibility: Int) {

    val logged = wishlistViewModel.username.isNotEmpty()

    LaunchedEffect(visibility) {
        wishlistViewModel.loadWishlists(visibility)
    }

    if (!logged) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text("Devi prima effettuare il login per visualizzare le tue liste di desideri!", style = TextStyle(fontSize = 16.sp))
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(wishlistViewModel.wishlists) { wishlist ->
                TextButton(
                    onClick = {
                        wishlistViewModel.addProductToWishlist(wishlist.name, visibility)
                    }) {
                    Text(wishlist.name, style = TextStyle(fontSize = 16.sp))
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
