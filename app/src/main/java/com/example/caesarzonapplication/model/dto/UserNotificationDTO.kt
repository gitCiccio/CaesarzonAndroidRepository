package com.example.caesarzonapplication.model.dto

import java.time.LocalDate
import java.util.UUID

data class UserNotificationDTO (
    val id : UUID,
    val date : String,
    val subject : String,
    val user : String,
    val read : Boolean,
    val explanation : String
)