package com.example.caesarzonapplication.model.dto

import java.io.Serializable

data class SendProductDTO(
    val id: Serializable,
    val name: String,
    val description: String,
    val brand: String,
    val price: Double,
    val discount: Double,
    val primaryColor: String,
    val secondaryColor: String,
    val is_clothing: Boolean,
    val sport: String,
    val availabilities: List<SendAvailabilityDTO>,
)
