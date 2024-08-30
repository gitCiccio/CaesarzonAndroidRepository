package com.example.caesarzonapplication.model.dto.productDTOS

data class BuyDTO(
    val addressID: String,
    val cardID: String,
    val total: Double,
    val productsIds: List<String>
) {
}