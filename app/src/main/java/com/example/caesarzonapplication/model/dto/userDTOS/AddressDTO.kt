package com.example.caesarzonapplication.model.dto.userDTOS

data class AddressDTO(
    var id: String,
    val roadName: String,
    val houseNumber: String,
    val roadType: String,
    val city: CityDataDTO
)
