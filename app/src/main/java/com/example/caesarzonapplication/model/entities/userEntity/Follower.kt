package com.example.caesarzonapplication.model.entities.userEntity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "follower")
data class Follower(

    @ColumnInfo(name = "id")val id: UUID,
    @ColumnInfo(name = "id_username_utente2")val id_username_utente2: String,
    @ColumnInfo(name = "username_utente2")val username2: String,
    @ColumnInfo(name = "amico")val friend: Boolean
) {
}