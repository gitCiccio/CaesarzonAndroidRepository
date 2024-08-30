package com.example.caesarzonapplication.model.dto.productDTOS

data class ProductDTO(
    val id: String,
    val name: String,
    val description: String,
    val brand: String,
    val price: Double,
    val discount: Double,
    val primaryColor: String,
    val secondaryColor: String,
    val isClothing: Boolean,
    val sport: String,
    val availabilities: List<AvailabilityDTO>,
)