package com.example.caesarzonapplication.model

import java.util.UUID

data class ReportDTO(
    val reportDate: String,
    val reason: String,
    val description: String,
    val usernameUser1: String,
    val usernameUser2: String,
    val reviewId: UUID
)