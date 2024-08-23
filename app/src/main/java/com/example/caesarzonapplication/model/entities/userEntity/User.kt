package com.example.caesarzonapplication.model.entities.userEntity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "utente")
data class User(
    @PrimaryKey
    @ColumnInfo(name = "username")val username: String,
    @ColumnInfo(name = "nome")val firstName: String,
    @ColumnInfo(name = "cognome")val lastName: String,
    @ColumnInfo(name = "telefono")val phoneNumber: String,
    @ColumnInfo(name = "email")val email: String,
    @ColumnInfo(name = "password")var credentialValue: String,
    @ColumnInfo(name = "otp")val otp: String,

    ) {

}