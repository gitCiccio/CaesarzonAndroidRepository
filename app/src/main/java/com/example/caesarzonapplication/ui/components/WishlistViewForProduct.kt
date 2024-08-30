package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.globalUsername
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.logged
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.WishlistViewModel

@Composable
fun WishlistViewForProduct(wishlistViewModel: WishlistViewModel, productId: String, visibility: Int) {
    var popupMessage by rememberSaveable { mutableStateOf("") }
    var showPopup by rememberSaveable { mutableStateOf(false) }

    // Collect the appropriate state based on visibility
    val wishlists = when (visibility) {
        0 -> wishlistViewModel.wishlistsPublic.collectAsState()
        1 -> wishlistViewModel.wishlistsShared.collectAsState()
        2 -> wishlistViewModel.wishlistsPrivate.collectAsState()
        else -> wishlistViewModel.wishlistsPublic.collectAsState() // Default to public if unknown
    }

    if (showPopup) {
        GenericMessagePopup(message = popupMessage, onDismiss = { showPopup = false })
    }

    LaunchedEffect(visibility) {
        wishlistViewModel.getUserWishlists(globalUsername.value, visibility)
    }

    if (!logged.value) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Devi prima effettuare il login per visualizzare le tue liste di desideri!",
                style = TextStyle(fontSize = 16.sp)
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Iterate through the wishlists
            items(wishlists.value) { wishlist ->
                TextButton(
                    onClick = {
                        wishlistViewModel.addProductIntoList(wishlist.id, productId)
                        showPopup = true
                        popupMessage = "Prodotto aggiunto alla tua lista desideri: ${wishlist.name}"
                    }
                ) {
                    Text(wishlist.name, style = TextStyle(fontSize = 16.sp))
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
