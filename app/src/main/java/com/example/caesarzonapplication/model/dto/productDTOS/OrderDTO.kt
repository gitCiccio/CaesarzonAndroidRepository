package com.example.caesarzonapplication.model.dto.productDTOS

data class OrderDTO(
    val id: String,
    val orderNumber: String,
    val orderState: String,
    val expectedDeliveryDate: String,
    val purchaseDate: String,
    val refundDate: String,
    val refund: Boolean,
    val addressID: String,
    val cardID: String,
    val orderTotal: Double,
    val usernameString: String
)
