package com.example.caesarzonapplication.model.dto.notificationDTO

data class UserNotificationDTO (
    val id : String,
    val date : String,
    val subject : String,
    val user : String,
    val read : Boolean,
    val explanation : String
)