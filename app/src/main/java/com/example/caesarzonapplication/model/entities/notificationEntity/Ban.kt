package com.example.caesarzonapplication.model.entities.notificationEntity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "Ban")
data class Ban(
    @PrimaryKey
    @ColumnInfo(name = "id_ban")val banId: String,
    @ColumnInfo(name = "motivo")val reason: String,
    @ColumnInfo(name = "data_inizio")val startDate: LocalDate,
    @ColumnInfo(name = "data_fine")val endDate: LocalDate,
    @ColumnInfo(name = "username_utente")val userUsername: String,
    @ColumnInfo(name = "username_admin")val adminUsername: String
    ) {
}