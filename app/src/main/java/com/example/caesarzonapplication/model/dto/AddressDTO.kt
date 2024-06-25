package com.example.caesarzonapplication.model.dto

data class AddressDTO(
    val id: Int,
    val roadName: String,
    val houseNumber: String,
    val roadType: String,
    val city: CityDataDTO
)
