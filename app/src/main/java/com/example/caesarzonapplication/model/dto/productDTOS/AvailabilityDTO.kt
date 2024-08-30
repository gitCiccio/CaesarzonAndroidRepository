package com.example.caesarzonapplication.model.dto.productDTOS

import java.util.UUID

data class AvailabilityDTO (
    val id: String,
    val amount: Int,
    val size: String,
    val product: ProductDTO
)