package com.example.caesarzonapplication.model.dao.wishlistDao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.caesarzonapplication.model.entities.wishListEntity.Wishlist

@Dao
interface WishlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWishlist(wishlist: Wishlist)

    @Query("SELECT * FROM wishlist")
    fun getAllWishlist(): LiveData<List<Wishlist>>

    @Query("DELETE FROM wishlist WHERE id = :id")
    suspend fun deleteWishlistById(id: Long)

    @Query("DELETE FROM wishlist")
    fun deleteAllWishlist()


}
/*
*

class WishlistRepository @Inject constructor(
    private val wishlistDao: WishlistDao,
    private val wishlistProductDao: WishlistProductDao
) {
    @Transaction
    suspend fun insertWishlistWithProducts(wishlist: Wishlist, products: List<WishlistProduct>) {
        wishlistDao.insert(wishlist)
        products.forEach { wishlistProductDao.insert(it) }
    }
}
*/