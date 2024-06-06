package com.example.caesarzonapplication.model

import android.media.Image

data class Product(
    val name: String,
    val imageRes: Int,
    val price: Double,
    val description: String,
    val isInTheShoppingCart: Boolean,
    val quantity: Int
)
