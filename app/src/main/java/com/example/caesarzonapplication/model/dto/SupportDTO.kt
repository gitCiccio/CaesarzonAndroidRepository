package com.example.caesarzonapplication.model.dto

data class SupportDTO(
    val id: String,
    val username: String,
    val type: String,
    val subject: String,
    val text: String,
    var dateRequest: String
)