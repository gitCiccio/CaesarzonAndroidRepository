package com.example.caesarzonapplication.model

data class User (
    val username: String,
    var isFollower: Boolean,
    var isFriend: Boolean,
    var isLogged: Boolean,
    val access_token: String,
    val refresh_token: String

)