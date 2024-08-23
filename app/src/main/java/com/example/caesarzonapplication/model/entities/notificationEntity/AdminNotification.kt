package com.example.caesarzonapplication.model.entities.notificationEntity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate
@Entity(
    tableName = "notifiche_admin",
    foreignKeys = [
        ForeignKey(
            entity = Report::class,
            parentColumns = ["id"],
            childColumns = ["id_segnalazione"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Support::class,
            parentColumns = ["id"],
            childColumns = ["id_richiesta_di_supporto"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("id_segnalazione"), Index("id_richiesta_di_supporto")]
)
data class AdminNotification(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0,

    @ColumnInfo(name = "id_notifche_admin") val idAdminNotification: String,

    @ColumnInfo(name = "data") val date: LocalDate,  // Richiede un TypeConverter

    @ColumnInfo(name = "descrizione") val description: String,

    @ColumnInfo(name = "username_admin") val admin: String,

    @ColumnInfo(name = "letta") val read: Boolean,

    @ColumnInfo(name = "id_segnalazione") val report: Long,  // Corretto, deve corrispondere a Report.id

    @ColumnInfo(name = "id_richiesta_di_supporto") val support: Long  // Corretto, deve corrispondere a Support.id
)

