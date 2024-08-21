package com.example.caesarzonapplication.model.repository.productRepository

import com.example.caesarzonapplication.model.dao.productDao.ProductOrderDao
import com.example.caesarzonapplication.model.entities.shoppingCartEntities.ProductOrder

class ProductOrderRepository(private val productOrderDao: ProductOrderDao) {

    // Inserisce un nuovo ordine prodotto
    suspend fun addProductOrder(productOrder: ProductOrder): Boolean {
        return try {
            productOrderDao.addProductOrder(productOrder)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Recupera un ordine prodotto per id
    suspend fun getProductOrderById(id: Long): ProductOrder? {
        return try {
            val productOrder = productOrderDao.getProductOrderById(id)
            productOrder
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Recupera tutti gli ordini prodotto
    suspend fun getAllProductOrders(): List<ProductOrder> {
        return try {
            val productOrders = productOrderDao.getAllProductOrders()
            productOrders
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // Elimina un ordine prodotto per id
    suspend fun deleteProductOrderById(id: Long): Boolean {
        return try {
            productOrderDao.deleteProductOrderById(id)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Elimina tutti gli ordini prodotto
    suspend fun deleteAllProductOrders(): Boolean {
        return try {
            productOrderDao.deleteAllProductOrders()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

