package com.example.caesarzonapplication.model.dto.productDTOS

data class WishProductDTO(
    val visibility: String,
    val singleWishListProductDTOS: List<SingleWishlistProductDTO>
) {
}