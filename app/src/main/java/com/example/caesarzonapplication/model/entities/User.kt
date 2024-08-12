package com.example.caesarzonapplication.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "utenti")
data class User(

    @PrimaryKey(autoGenerate = true)
    val id: String = "",

    val firstName: String = "",
    val lastName: String = "",
    var username: String = "",
    val phoneNumber: String = "",
    var email: String = "",
    var isFollower: Boolean = false,
    var isFriend: Boolean = false
)

