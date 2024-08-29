package com.example.caesarzonapplication.model

import com.google.gson.annotations.SerializedName

data class TokenResponse(
     @SerializedName("access_token") var accessToken: String,
     @SerializedName("refresh_token") var refreshToken: String
)