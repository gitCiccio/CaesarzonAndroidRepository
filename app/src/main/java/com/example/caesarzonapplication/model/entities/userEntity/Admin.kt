package com.example.caesarzonapplication.model.entities.userEntity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "admin")
data class Admin(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0,

    @ColumnInfo(name = "nome") val firstName: String,

    @ColumnInfo(name = "cognome") val lastName: String,

    @ColumnInfo(name = "email") val email: String,

    @ColumnInfo(name = "password") val password: String,

    @ColumnInfo(name = "username") val username: String
)