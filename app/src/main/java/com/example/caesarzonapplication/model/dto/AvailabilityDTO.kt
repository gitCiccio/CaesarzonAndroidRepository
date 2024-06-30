package com.example.caesarzonapplication.model.dto

import java.util.UUID

data class AvailabilityDTO (
    val id: UUID,
    val amount: Int,
    val size: String,
    val productDTO: ProductDTO
)