package com.example.caesarzonapplication.model.dto

import java.util.UUID

data class ReviewDTO (
    val id : UUID,
    val text : String,
    val evaluation : Int,
    val username : String,
    val productID : UUID,
    val localDate : String

)