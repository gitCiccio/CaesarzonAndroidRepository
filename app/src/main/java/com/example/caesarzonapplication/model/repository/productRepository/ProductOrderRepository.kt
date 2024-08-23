package com.example.caesarzonapplication.model.repository.productRepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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


    // Recupera tutti gli ordini prodotto
    fun getAllProductOrders(): LiveData<List<ProductOrder>> {
        return try {
            val productOrders = productOrderDao.getAllProductOrders()
            productOrders
        } catch (e: Exception) {
            e.printStackTrace()
            MutableLiveData(emptyList())
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

