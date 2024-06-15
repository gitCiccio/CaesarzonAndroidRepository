package com.example.caesarzonapplication.model

import com.google.gson.annotations.SerializedName

data class TokenResponse(
     val accessToken: String,
     val refreshToken: String
)