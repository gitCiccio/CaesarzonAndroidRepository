package com.example.caesarzonapplication.model.dto

import kotlinx.coroutines.flow.MutableStateFlow

data class UserDTO (
    var id : String,
    var username: String,
    var firstName : String,
    var lastName : String,
    var phoneNumber : String,
    var email : String
)