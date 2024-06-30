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
    val wishlists = remember { mutableStateListOf<WishlistDTO>() }

    LaunchedEffect(visibility) {
        wishlistViewModel.loadWishlists(visibility)
        wishlists.clear()
        wishlists.addAll(wishlistViewModel.wishlists)
    }

    if (wishlists.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text("Non hai creato ancora nessuna lista dei desideri!", style = TextStyle(fontSize = 16.sp))
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(wishlists) { wishlist ->
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
