package com.example.caesarzonapplication.model.dao.wishlistDao

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
    suspend fun findAllByWishlistId(wishlistId: Long): List<WishlistProduct>

    @Query("SELECT * FROM prodotto_lista_desideri WHERE id_prodotto = :productId AND id_lista_desideri = :wishlistId")
    suspend fun findByProductAndWishlistfindByProductAndWishlist(productId: String, wishlistId: Long): WishlistProduct?

    @Query("DELETE FROM prodotto_lista_desideri WHERE id_lista_desideri = :wishlistId")
    suspend fun deleteWishListProductByWishlistId(wishlistId: Long)

    @Query("DELETE FROM prodotto_lista_desideri")
    fun deleteAllWishlistProducts()

}