package com.example.caesarzonapplication.model.entities.userEntity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "follower")
data class Follower(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")val id: Long = 0,
    @ColumnInfo(name = "username_utente")val username2: String,
    @ColumnInfo(name = "amico")val friend: Boolean
) {
}