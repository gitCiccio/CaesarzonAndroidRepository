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
            parentColumns = ["id"],
            childColumns = ["id_prodtto"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("id_prodtto")]
)
data class ProductOrder(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")val id: Long = 0,
    @ColumnInfo(name = "id_ordine_rpdotto")val prodcutOrderId: String,
    @ColumnInfo(name = "id_prodtto")val productId: Long,
    @ColumnInfo(name = "totale")val total: Double,
    @ColumnInfo(name = "id_ordine")val orderId: String,
    @ColumnInfo(name = "quantità")val quantity: Int,
    @ColumnInfo(name = "per_dopo")val buyLater: Boolean,
    @ColumnInfo(name = "taglia")val size: String
)
