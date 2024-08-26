package com.example.caesarzonapplication.model.repository.productRepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.caesarzonapplication.model.dao.productDao.ProductDao
import com.example.caesarzonapplication.model.entities.shoppingCartEntities.Product

class ProductRepository(private val productDao: ProductDao) {

    suspend fun addProduct(product: Product): Boolean {
        return try {
            productDao.addProduct(product)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    fun getAllProducts(): LiveData<List<Product>> {
        return try {
            val products = productDao.getAllProducts()
            products
        } catch (e: Exception) {
            e.printStackTrace()
            MutableLiveData(emptyList())
        }
    }

    suspend fun deleteProductById(id: String): Boolean {
        return try {
            productDao.deleteProductById(id)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteAllProducts(): Boolean {
        return try {
            productDao.deleteAllProducts()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

