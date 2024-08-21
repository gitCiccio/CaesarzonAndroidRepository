package com.example.caesarzonapplication.model.dao.productDao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.caesarzonapplication.model.entities.shoppingCartEntities.Product

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addProduct(product: Product)

    @Query("SELECT * FROM prodotto WHERE id = :id")
    suspend fun getProductById(id: Long): Product?

    @Query("SELECT * FROM prodotto")
    suspend fun getAllProducts(): List<Product>

    @Query("DELETE FROM prodotto WHERE id = :id")
    suspend fun deleteProductById(id: Long)

    @Query("DELETE FROM prodotto")
    fun deleteAllProducts()
}