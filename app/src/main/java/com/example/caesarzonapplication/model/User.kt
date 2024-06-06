package com.example.caesarzonapplication.model

data class User (
    val username: String,
    var isFollower: Boolean,
    var isFriend: Boolean
)