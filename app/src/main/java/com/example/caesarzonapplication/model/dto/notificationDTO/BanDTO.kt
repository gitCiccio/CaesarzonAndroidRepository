package com.example.caesarzonapplication.model.dto.notificationDTO

data class BanDTO (
    val reason: String,
    val startDate: String,
    val endDate: String,
    val userUsername: String,
    val adminUsername: String

)