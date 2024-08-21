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
            parentColumns = ["id"],
            childColumns = ["id_lista_desideri"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["id_lista_desideri"])]
)
data class WishlistProduct(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "id_prodotto") val productId: String,
    @ColumnInfo(name = "nome") val name: String,
    @ColumnInfo(name = "immagine", typeAffinity = ColumnInfo.BLOB) val image: Bitmap,
    @ColumnInfo(name = "id_lista_desideri") val wishlistId: Long // Colonna che funge da foreign key
) {

}