package com.example.caesarzonapplication.model.repository.productRepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.caesarzonapplication.model.dao.productDao.ProductImageDao
import com.example.caesarzonapplication.model.entities.shoppingCartEntities.ProductImage

class ProductImageRepository(private val productImageDao: ProductImageDao) {

    // Inserisce una nuova immagine di prodotto
    suspend fun insertProductImage(productImage: ProductImage): Boolean {
        return try {
            productImageDao.insertProductImage(productImage)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Recupera tutte le immagini associate a un prodotto
    fun getProductImagesByProductId(productId: String): LiveData<List<ProductImage>> {
        return try {
            productImageDao.getProductImagesByProductId(productId)
        } catch (e: Exception) {
            e.printStackTrace()
            MutableLiveData(emptyList())
        }
    }

    // Elimina un'immagine di prodotto per ID
    suspend fun deleteProductImageById(id: String): Boolean {
        return try {
            productImageDao.deleteProductImageById(id)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Elimina tutte le immagini di prodotto
    suspend fun deleteAllProductImages(): Boolean {
        return try {
            productImageDao.deleteAllProductImages()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
