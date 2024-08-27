package com.example.caesarzonapplication.model.dto

data class CardDTO(
    val id: String,
    val cardNumber: String,
    val owner: String,
    val cvv: String,
    val expiryDate: String,
    val balance: Double
) {
}