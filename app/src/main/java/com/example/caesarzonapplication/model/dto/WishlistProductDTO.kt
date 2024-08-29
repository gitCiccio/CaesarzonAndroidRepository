package com.example.caesarzonapplication.model.dto

import java.util.UUID

data class WishlistProductListDTO (
    val id: String,
    val wishlistDTO: WishlistDTO,
    val productDTO: ProductDTO,
    val onDeleting: Boolean
)