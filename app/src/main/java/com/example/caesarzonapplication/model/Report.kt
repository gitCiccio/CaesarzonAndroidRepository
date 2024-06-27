package com.example.caesarzonapplication.model

data class Report(
    val code: String,
    val reportedUser: String,
    val reason: String,
    val date: String,
    val actions: List<String>
)