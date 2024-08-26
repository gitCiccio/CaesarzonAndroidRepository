package com.example.caesarzonapplication.model.entities.notificationEntity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate@Entity(
    tableName = "notifiche_admin",
    foreignKeys = [
        ForeignKey(
            entity = Report::class,
            parentColumns = ["id_segnalazione"], // Corrected to match Report primary key
            childColumns = ["id_segnalazione"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Support::class,
            parentColumns = ["id_richiesta_di_supporto"], // Corrected to match Support primary key
            childColumns = ["id_richiesta_di_supporto"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("id_segnalazione"), Index("id_richiesta_di_supporto")]
)
data class AdminNotification(
    @PrimaryKey
    @ColumnInfo(name = "id_notifche_admin") val idAdminNotification: String,

    @ColumnInfo(name = "data") val date: LocalDate,  // Requires a TypeConverter for LocalDate

    @ColumnInfo(name = "descrizione") val description: String,

    @ColumnInfo(name = "username_admin") val admin: String,

    @ColumnInfo(name = "letta") val read: Boolean,

    @ColumnInfo(name = "id_segnalazione") val report: String,  // Should match the type of Report.id_segnalazione

    @ColumnInfo(name = "id_richiesta_di_supporto") val support: String  // Should match the type of Support.id_richiesta_di_supporto
)

