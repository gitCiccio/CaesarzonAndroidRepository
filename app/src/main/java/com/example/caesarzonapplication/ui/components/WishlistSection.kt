package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.caesarzonapplication.model.dto.WishlistDTO
import com.example.caesarzonapplication.viewmodels.WishlistViewModel

@Composable
fun WishlistSection (visibility: Int, wishlistViewModel: WishlistViewModel) {

    wishlistViewModel.loadWishlists(visibility)

    Column {
        for (wishlist in wishlistViewModel.wishlists) {
            Row {
                Text(text = wishlist.name)
                Button(onClick = { /* Visualizza lista */ }) {
                    Text(text = "Visualizza")
                }
                Button(onClick = { /* Elimina lista */ }) {
                    Text(text = "Elimina")
                }
                Button(onClick = { /* Svuota lista */ }) {
                    Text(text = "Svuota")
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
        }
    }
}
