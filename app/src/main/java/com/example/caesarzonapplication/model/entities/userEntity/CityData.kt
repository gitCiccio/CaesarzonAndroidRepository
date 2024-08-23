package com.example.caesarzonapplication.model.entities.userEntity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "dati_comune")
data class CityData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")val id: Long=0,
    @ColumnInfo(name = "id_dati_comune")val id_city_data: String,
    @ColumnInfo(name = "nome_comune")val city: String,
    @ColumnInfo(name = "cap")var cap: String,
    @ColumnInfo(name = "regione")var region: String,
    @ColumnInfo(name = "provincia")var province: String
) {
}