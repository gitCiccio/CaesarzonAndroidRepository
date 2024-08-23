package com.example.caesarzonapplication.model.dao.wishlistDao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.caesarzonapplication.model.entities.wishListEntity.WishlistProduct

@Dao
interface WishlistProductDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addWishlistProduct(wishlistProductDao: WishlistProduct)

    @Query("SELECT * FROM prodotto_lista_desideri WHERE id_lista_desideri = :wishlistId")
    fun findAllByWishlistId(wishlistId: Long): LiveData<List<WishlistProduct>>


    @Query("DELETE FROM prodotto_lista_desideri WHERE id_lista_desideri = :wishlistId")
    suspend fun deleteWishListProductByWishlistId(wishlistId: Long)

    @Query("DELETE FROM prodotto_lista_desideri")
    fun deleteAllWishlistProducts()

}