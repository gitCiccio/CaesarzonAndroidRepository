package com.example.caesarzonapplication.model.dto.notificationDTO

import java.util.UUID

data class ReportDTO(
    val id: String,
    val reportDate: String,
    val reason: String,
    val description: String,
    val usernameUser1: String,
    val usernameUser2: String,
    val explain: String,
    val reviewText: String,
    val reviewId: UUID
)