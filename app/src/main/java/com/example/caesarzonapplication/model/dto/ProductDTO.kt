package com.example.caesarzonapplication.model.dto

data class ProductDTO (
    val id: Int ?,
    val name: String,
    val description: String,
    val brand: String,
    val price: Double,
    val discount: Double,
    val primaryColor: String,
    val secondaryColor: String,
    val is_clothing: Boolean,
    val sport: String,
    val availabilities: List<AvailabilityDTO>,
)