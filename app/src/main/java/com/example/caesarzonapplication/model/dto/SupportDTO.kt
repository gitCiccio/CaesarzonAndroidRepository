package com.example.caesarzonapplication.model.dto

import java.util.UUID

data class SupportDTO(
    val id: UUID,
    val username: String,
    val type: String,
    val subject: String,
    val text: String,
    val localDate: String
)