package com.example.caesarzonapplication.model.dto

import java.util.UUID

data class SingleWishlistProductDTO (
    val productId: UUID,
    val productName : String,
    val price : Double
)