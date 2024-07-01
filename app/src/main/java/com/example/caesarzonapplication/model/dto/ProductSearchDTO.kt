package com.example.caesarzonapplication.model.dto

import java.util.UUID

data class ProductSearchDTO(
    val productName: String,
    val productId: UUID,
    val averageReview: Double,
    val reviewNumber: Int,
    val price: Double,
    val discount: Int
)
