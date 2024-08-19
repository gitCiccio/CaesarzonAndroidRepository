package com.example.caesarzonapplication.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "followers")
data class Follower(

    @PrimaryKey(autoGenerate = true)
    val id: String = "",

    val firstName: String = "",
    val lastName: String = "",
    var username: String = "",
    var email: String = "",
    var isFriend: Boolean = false
)

