package com.example.caesarzonapplication.model.dto

data class WishProductDTO(
    val visibility: String,
    val singleWishListProductDTOS: List<SingleWishlistProductDTO>
) {
}