package com.example.caesarzonapplication.model.dto.userDTOS

import android.graphics.Bitmap

data class UserFindDTO(
    val username: String,
    val profileImage: Bitmap?
)