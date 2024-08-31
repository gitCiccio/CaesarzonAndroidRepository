package com.example.caesarzonapplication.model.dto.productDTOS

import java.io.Serializable

data class SendAvailabilityDTO (
    val amount: Int,
    val size: String,
): Serializable
