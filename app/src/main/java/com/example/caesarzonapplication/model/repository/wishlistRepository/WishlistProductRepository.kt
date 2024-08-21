package com.example.caesarzonapplication.model.repository.wishlistRepository

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

    suspend fun findAllByWishlistId(wishlistId: Long): List<WishlistProduct> {
        return try {
            val wishlistProducts = wishlistProductDao.findAllByWishlistId(wishlistId)
            wishlistProducts
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun findByProductAndWishlist(productId: String, wishlistId: Long): WishlistProduct? {
        return try {
            val wishlistProduct = wishlistProductDao.findByProductAndWishlistfindByProductAndWishlist(productId, wishlistId)
            wishlistProduct
        } catch (e: Exception) {
            e.printStackTrace()
            null
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
