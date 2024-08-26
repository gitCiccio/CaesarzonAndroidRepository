package com.example.caesarzonapplication.model.entities.shoppingCartEntities

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "foto_prodotti",
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id_prodotto"], // Corrected to match Product primary key
            childColumns = ["id_prodotto"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("id_prodotto")]
)
class ProductImage(
    @PrimaryKey
    @ColumnInfo(name = "id_foto_prodotto") val id: String,
    @ColumnInfo(name = "id_prodotto") val productId: String, // Foreign key reference to Product entity
    @ColumnInfo(name = "immagine", typeAffinity = ColumnInfo.BLOB) val imageRes: Bitmap
)