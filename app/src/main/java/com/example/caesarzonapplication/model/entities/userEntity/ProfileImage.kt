package com.example.caesarzonapplication.model.entities.userEntity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foto_utente")
class ProfileImage(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "username")val username: String,
    @ColumnInfo(name = "foto", typeAffinity = ColumnInfo.BLOB)val profilePicture: Bitmap
) {
}