package com.example.caesarzonapplication.model.entities.wishListEntity

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
@Entity(
    tableName = "prodotto_lista_desideri",
    foreignKeys = [
        ForeignKey(
            entity = Wishlist::class,
            parentColumns = ["id_lista_desideri"], // Correct primary key column of Wishlist
            childColumns = ["id_lista_desideri_prodotto"], // Correct foreign key column in WishlistProduct
            onDelete = ForeignKey.CASCADE // Optional: cascades delete operation
        )
    ],
    indices = [Index("id_lista_desideri_prodotto")] // Index for faster lookups
)
data class WishlistProduct(
    @PrimaryKey
    @ColumnInfo(name = "id_prodotto") val productId: String,
    @ColumnInfo(name = "nome") val name: String,
    @ColumnInfo(name = "immagine", typeAffinity = ColumnInfo.BLOB) val image: Bitmap,
    @ColumnInfo(name = "id_lista_desideri_prodotto") val wishlistId: String // Foreign key column
)
