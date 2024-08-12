package com.example.caesarzonapplication.model.entities

data class Product(
    val name: String,
    val imageRes: Int,
    val price: Double,
    val description: String,
    val isInTheShoppingCart: Boolean,
    var quantity: Int
)
