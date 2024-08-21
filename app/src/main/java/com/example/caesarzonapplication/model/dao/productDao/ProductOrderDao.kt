package com.example.caesarzonapplication.model.dao.productDao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.caesarzonapplication.model.entities.shoppingCartEntities.ProductOrder

@Dao
interface ProductOrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProductOrder(productOrder: ProductOrder)

    @Query("SELECT * FROM ordine_prodotto WHERE id = :id")
    suspend fun getProductOrderById(id: Long): ProductOrder?

    @Query("SELECT * FROM ordine_prodotto")
    suspend fun getAllProductOrders(): List<ProductOrder>

    @Query("DELETE FROM ordine_prodotto WHERE id = :id")
    suspend fun deleteProductOrderById(id: Long)

    @Query("DELETE FROM ordine_prodotto")
    fun deleteAllProductOrders()
}