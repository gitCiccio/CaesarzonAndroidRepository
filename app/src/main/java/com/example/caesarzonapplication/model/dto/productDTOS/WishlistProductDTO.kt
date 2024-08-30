package com.example.caesarzonapplication.model.dto.productDTOS

data class WishlistProductListDTO (
    val id: String,
    val wishlistDTO: WishlistDTO,
    val productDTO: ProductDTO,
    val onDeleting: Boolean
)