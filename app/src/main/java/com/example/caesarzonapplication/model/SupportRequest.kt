package com.example.caesarzonapplication.model

data class SupportRequest(
    val code: String,
    val user: String,
    val type: String,
    val date: String,
    val actions: String
)