package com.example.caesarzonapplication.model.entities.wishListEntity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist")
data class Wishlist(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "id_lista_desideri") val productId: String,
    @ColumnInfo(name = "nome") val name: String,
    @ColumnInfo(name = "visibilità")val visibility: Boolean
)
