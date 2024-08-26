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

    @Query("SELECT * FROM ordine_prodotto")
    fun getAllProductOrders(): LiveData<List<ProductOrder>>

    @Query("DELETE FROM ordine_prodotto WHERE id_ordine_prodotto = :id")
    suspend fun deleteProductOrderById(id: String)

    @Query("DELETE FROM ordine_prodotto")
    fun deleteAllProductOrders()
}