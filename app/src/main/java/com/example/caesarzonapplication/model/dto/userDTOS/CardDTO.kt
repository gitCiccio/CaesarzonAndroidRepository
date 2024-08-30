package com.example.caesarzonapplication.model.dto.userDTOS

data class CardDTO(
    var id: String,
    val cardNumber: String,
    val owner: String,
    val cvv: String,
    val expiryDate: String,
    val balance: Double
) {
}