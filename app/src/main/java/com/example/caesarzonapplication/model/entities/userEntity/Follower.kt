package com.example.caesarzonapplication.model.entities.userEntity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "follower")
data class Follower(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")val id: Long = 0,
    @ColumnInfo(name = "id_amico")val id_follower: String,
    @ColumnInfo(name = "id_username_utente2")val id_username_utente2: String,
    @ColumnInfo(name = "username_utente2")val username2: String,
    @ColumnInfo(name = "amico")val friend: Boolean
) {
}