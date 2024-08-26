package com.example.caesarzonapplication.model.entities.shoppingCartEntities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ordine_prodotto",
    foreignKeys = [
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id_prodotto"], // Should refer to Product's primary key
            childColumns = ["ref_prodotto"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("ref_prodotto")] // Corrected the typo
)
data class ProductOrder(
    @PrimaryKey
    @ColumnInfo(name = "id_ordine_prodotto") val productOrderId: String,
    @ColumnInfo(name = "ref_prodotto") val productId: String,
    @ColumnInfo(name = "totale") val total: Double,
    @ColumnInfo(name = "id_ordine") val orderId: String,
    @ColumnInfo(name = "quantità") val quantity: Int,
    @ColumnInfo(name = "per_dopo") val buyLater: Boolean,
    @ColumnInfo(name = "taglia") val size: String
)
