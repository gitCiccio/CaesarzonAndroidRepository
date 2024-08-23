package com.example.caesarzonapplication.model.entities.notificationEntity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
@Entity(tableName = "richiesta_supporto")
data class Support(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0,

    @ColumnInfo(name = "id_richiesta_di_supporto") val support_id: String,

    @ColumnInfo(name = "tipo") val type: String,

    @ColumnInfo(name = "testo") val text: String,

    @ColumnInfo(name = "oggetto") val subject: String,

    @ColumnInfo(name = "data_richiesta") val dateRequest: LocalDate  // Richiede un TypeConverter
)

