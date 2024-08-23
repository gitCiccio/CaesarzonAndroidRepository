package com.example.caesarzonapplication.model.repository.wishlistRepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.caesarzonapplication.model.dao.wishlistDao.WishlistProductDao
import com.example.caesarzonapplication.model.entities.wishListEntity.WishlistProduct

class WishlistProductRepository(private val wishlistProductDao: WishlistProductDao) {

    suspend fun addWishlistProduct(wishlistProduct: WishlistProduct): Boolean {
        return try {
            wishlistProductDao.addWishlistProduct(wishlistProduct)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun findAllByWishlistId(wishlistId: Long): LiveData<List<WishlistProduct>> {
        return try {
            val wishlistProducts = wishlistProductDao.findAllByWishlistId(wishlistId)
            wishlistProducts
        } catch (e: Exception) {
            e.printStackTrace()
            MutableLiveData(emptyList())
        }
    }

    suspend fun deleteWishListProductByWishlistId(wishlistId: Long): Boolean {
        return try {
            wishlistProductDao.deleteWishListProductByWishlistId(wishlistId)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
