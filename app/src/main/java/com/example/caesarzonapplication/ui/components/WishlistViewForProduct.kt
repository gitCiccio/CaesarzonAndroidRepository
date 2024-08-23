package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caesarzonapplication.model.dto.WishlistDTO
import com.example.caesarzonapplication.model.viewmodels.WishlistViewModel

@Composable
fun WishlistViewForProduct(wishlistViewModel: WishlistViewModel, visibility: Int) {

    //val logged = wishlistViewModel.username.isNotEmpty()

    var popupMessage by rememberSaveable { mutableStateOf("") }
    var showPopup by rememberSaveable { mutableStateOf(false) }

    if (showPopup) {
        GenericMessagePopup(message = popupMessage, onDismiss = { showPopup = false })
    }

    LaunchedEffect(visibility) {
        wishlistViewModel.loadWishlists(visibility)
    }

    if (!true) {
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
                        showPopup = true
                        popupMessage = "Prodotto aggiunto alla tua lista desideri: " + wishlist.name
                    }) {
                    Text(wishlist.name, style = TextStyle(fontSize = 16.sp))
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
