package com.example.caesarzonapplication.model.dto

import java.util.UUID

data class AdminNotificationDTO (
     val id: UUID,
     val date: String,
     val subject: String,
     val admin: String,
     val read: Boolean,
     val reportId: UUID,
     val supportId: UUID
)