package com.example.caesarzonapplication.model.entities.shoppingCartEntities

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "foto_prodotti",
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["id_prodotto"],
            onDelete = ForeignKey.CASCADE
        )
    ], indices = [androidx.room.Index("id_prodotto")])
class ProductImage(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "id_prodotto") val productId: String,
    @ColumnInfo(name = "immagine", typeAffinity = ColumnInfo.BLOB) val imageRes: Bitmap
) {
}