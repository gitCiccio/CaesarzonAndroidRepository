package com.example.caesarzonapplication.model.repository.productRepository

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

    suspend fun getProductById(id: Long): Product? {
        return try {
            val product = productDao.getProductById(id)
            product
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getAllProducts(): List<Product> {
        return try {
            val products = productDao.getAllProducts()
            products
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun deleteProductById(id: Long): Boolean {
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

