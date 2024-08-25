package com.example.caesarzonapplication.model.entities.shoppingCartEntities

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "prodotto")
data class Product(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "id_prodotto") val productId: String,
    @ColumnInfo(name = "descrizione") val description: String,
    @ColumnInfo(name = "nome") val name: String,
    @ColumnInfo(name = "marca") val brand: String,
    @ColumnInfo(name = "sconto") val discount: Int,
    @ColumnInfo(name = "prezzo") val price: Double,
    @ColumnInfo(name = "colore_primario") val primaryColor: String,
    @ColumnInfo(name = "colore_secondari") val secondaryColor: String,
    @ColumnInfo(name = "e_abbigliamento") val is_clothing: Boolean,
    @ColumnInfo(name = "sport") val sport: String,
    @ColumnInfo(name = "ultima_aggiunta") val lastModified: LocalDate,
    @ColumnInfo(name = "quantita") val quantity: Int,
)
