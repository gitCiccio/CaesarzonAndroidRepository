package com.example.caesarzonapplication.model

data class Ban(
    val date: String,
    val bannedUser: String,
    val reason: String,
    val actions: List<String>
)