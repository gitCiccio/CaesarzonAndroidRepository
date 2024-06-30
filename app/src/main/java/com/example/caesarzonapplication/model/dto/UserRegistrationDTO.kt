package com.example.caesarzonapplication.model.dto

data class UserRegistrationDTO (
    var firstName: String,
    var lastName: String,
    var username: String,
    var email: String,
    var credentialValue: String
)