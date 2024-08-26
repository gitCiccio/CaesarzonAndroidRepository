package com.example.caesarzonapplication.model.entities.userEntity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "indirizzo")
data class Address(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "id_indirizzo")val address_id: String,
    @ColumnInfo(name = "nome_strada")val streetName: String,
    @ColumnInfo(name = "numero_civico")val streetNumber: String,
    @ColumnInfo(name = "tipo_strada")val roadType: String,
    @ColumnInfo(name = "id_dati_comune")val city: CityData,
    ){

}
