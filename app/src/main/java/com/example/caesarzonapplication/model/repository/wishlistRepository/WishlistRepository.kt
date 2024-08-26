package com.example.caesarzonapplication.model.repository.wishlistRepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.caesarzonapplication.model.dao.wishlistDao.WishlistDao
import com.example.caesarzonapplication.model.entities.wishListEntity.Wishlist

class WishlistRepository(private val wishlistDao: WishlistDao) {

    suspend fun addWishlist(wishlist: Wishlist): Boolean {
        return try {
            wishlistDao.addWishlist(wishlist)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }



    fun getAllWishlist(): LiveData<List<Wishlist>> {
        return try {
            val wishlists = wishlistDao.getAllWishlist()
            wishlists
        } catch (e: Exception) {
            e.printStackTrace()
            MutableLiveData(emptyList())
        }
    }

    suspend fun deleteWishlistById(id: String): Boolean {
        return try {
            wishlistDao.deleteWishlistById(id)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteAllWishlist(): Boolean {
        return try {
            wishlistDao.deleteAllWishlist()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
