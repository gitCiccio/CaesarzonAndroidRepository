package com.example.caesarzonapplication.model.dto.productDTOS

import com.fasterxml.jackson.annotation.ObjectIdGenerators.StringIdGenerator

data class ProductDTO(
    val id: String,
    val name: String,
    val description: String,
    val brand: String,
    val price: Double,
    val discount: Double,
    val primaryColor: String,
    val secondaryColor: String,
    val is_clothing: Boolean,
    val availabilities: List<AvailabilitiesSingle>,
    val sport: String,
    val lastModified: String
)