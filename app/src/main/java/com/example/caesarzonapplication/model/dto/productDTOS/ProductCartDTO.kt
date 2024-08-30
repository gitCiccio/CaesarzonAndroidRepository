package com.example.caesarzonapplication.model.dto.productDTOS

data class ProductCartDTO(
    val id: String,
    val total: Double,
    val discountTotal: Double,
    var quantity: Int,
    val name: String,
    var size: String,
    val buyLater: Boolean
)
