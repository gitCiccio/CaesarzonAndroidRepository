package com.example.caesarzonapplication.model.entities.userEntity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "carte")
data class Card(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")val id: Long = 0,
    @ColumnInfo(name = "id_carta")val id_carta: String,
    @ColumnInfo(name = "numero_carta")val cardNumber: String,
    @ColumnInfo(name = "titolare")val owner: String,
    @ColumnInfo(name = "cvv")val cvv: String,
    @ColumnInfo(name = "data_scadenza")val expirationDate: String,
    @ColumnInfo(name = "saldo")val balance: Double
) {
}