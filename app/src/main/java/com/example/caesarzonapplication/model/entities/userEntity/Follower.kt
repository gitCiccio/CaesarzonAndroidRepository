package com.example.caesarzonapplication.model.entities.userEntity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "follower")
data class Follower(
    @PrimaryKey
    @ColumnInfo(name = "id")val id: String,
    @ColumnInfo(name = "username_utente")val username2: String,
    @ColumnInfo(name = "amico")val friend: Boolean
) {
}