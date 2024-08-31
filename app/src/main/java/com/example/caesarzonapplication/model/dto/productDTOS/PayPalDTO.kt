package com.example.caesarzonapplication.model.dto.productDTOS

data class PayPalDTO(
    val paymentId: String,
    val token: String,
    val payerId: String,
    val buyDTO: BuyDTO
){

}
