package com.example.caesarzonapplication.model.dto

import java.util.UUID

data class ReportDTO(
    val id: UUID,
    val reportDate: String,
    val reason: String,
    val description: String,
    val usernameUser1: String,
    val usernameUser2: String,
    val reviewId: UUID
)