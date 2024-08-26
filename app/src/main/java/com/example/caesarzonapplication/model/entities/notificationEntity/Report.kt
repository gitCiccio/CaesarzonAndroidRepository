package com.example.caesarzonapplication.model.entities.notificationEntity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
@Entity(tableName = "segnala")
data class Report(
    @PrimaryKey
    @ColumnInfo(name = "id_segnalazione") val id_segnalazione: String,

    @ColumnInfo(name = "data_segnalazione") val reportDate: LocalDate,  // Richiede un TypeConverter

    @ColumnInfo(name = "motivo") val reason: String,

    @ColumnInfo(name = "descrizione") val description: String,

    @ColumnInfo(name = "username_utente2") val usernameUser2: String,

    @ColumnInfo(name = "id_recensione") val reviewId: String
)
