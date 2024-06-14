package com.example.caesarzonapplication.model.dto

import java.util.UUID

data class FollowerDTO(
    var id: UUID,
    var userUsername1: String,
    var userUsername2: String,
    var friendStatus: Boolean
,
)