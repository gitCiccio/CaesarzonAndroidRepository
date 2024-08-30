package com.example.caesarzonapplication.model.dto.authDTOS

data class UserRegistrationDTO (
    var firstName: String,
    var lastName: String,
    var username: String,
    var email: String,
    var credentialValue: String,
)