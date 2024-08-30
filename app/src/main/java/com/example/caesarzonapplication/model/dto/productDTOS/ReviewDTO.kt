package com.example.caesarzonapplication.model.dto.productDTOS

import java.util.UUID

data class ReviewDTO (
    val id : UUID,
    val text : String,
    val evaluation : Int,
    val username : String,
    val productID : UUID,
    val date : String

)