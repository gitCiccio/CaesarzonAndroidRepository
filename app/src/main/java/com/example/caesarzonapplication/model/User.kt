package com.example.caesarzonapplication.model

import androidx.compose.runtime.MutableState


data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    var username: String = "",
    val phoneNumber: String = "",
    var email: String = "",
    var isFollower: Boolean = false,
    var isFriend: Boolean = false
)

