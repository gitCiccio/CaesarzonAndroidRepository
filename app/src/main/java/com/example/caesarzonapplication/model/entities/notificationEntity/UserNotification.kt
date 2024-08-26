package com.example.caesarzonapplication.model.entities.notificationEntity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "notifiche_utente")
data class UserNotification(
    @PrimaryKey
    @ColumnInfo(name = "id_notifiche_utente")val id_username_notification: String,
    @ColumnInfo(name = "data")val localDate: LocalDate,
    @ColumnInfo(name = "descrizione")val subject: String,
    @ColumnInfo(name = "username_utente")val user: String,
    @ColumnInfo(name = "letta")val read: Boolean,
    @ColumnInfo(name = "spiegazione")val explanation: String
)
