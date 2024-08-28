package com.example.caesarzonapplication.model.dto

import com.google.gson.annotations.SerializedName

data class UserSearchDTO(
    @SerializedName("username")var username: String,
    @SerializedName("follower")var follower: Boolean,
    @SerializedName("friend")var friend: Boolean,
)