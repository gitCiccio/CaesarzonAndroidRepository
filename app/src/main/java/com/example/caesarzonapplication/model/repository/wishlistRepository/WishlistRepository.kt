package com.example.caesarzonapplication.model.repository.wishlistRepository

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

    suspend fun getWishlistById(id: Int): Wishlist? {
        return try {
            val wishlist = wishlistDao.getWishlistById(id)
            wishlist
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getAllWishlist(): List<Wishlist> {
        return try {
            val wishlists = wishlistDao.getAllWishlist()
            wishlists
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun deleteWishlistById(id: Long): Boolean {
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
