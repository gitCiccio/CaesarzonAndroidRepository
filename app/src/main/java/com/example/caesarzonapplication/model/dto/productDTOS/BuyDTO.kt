package com.example.caesarzonapplication.model.dto.productDTOS

import java.io.Serializable

data class BuyDTO(
    val addressID: String,
    val cardID: String,
    val total: Double,
    val productsIds: List<String>
) :Serializable{
}