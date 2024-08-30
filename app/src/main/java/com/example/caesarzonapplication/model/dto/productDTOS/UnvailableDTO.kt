package com.example.caesarzonapplication.model.dto.productDTOS

data class UnvailableDTO(
    val id: String,
    val name: String,
    val availabilities: List<AvailabilityDTO>
)
