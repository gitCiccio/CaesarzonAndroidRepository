package com.example.caesarzonapplication.model.dao.productDao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.caesarzonapplication.model.entities.shoppingCartEntities.ProductImage

@Dao
interface ProductImageDao {

    // Inserisce una nuova immagine di prodotto
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductImage(productImage: ProductImage)

    // Recupera tutte le immagini associate a un prodotto
    @Query("SELECT * FROM foto_prodotti WHERE id_prodotto = :productId")
    fun getProductImagesByProductId(productId: String): LiveData<List<ProductImage>>

    // Elimina un'immagine di prodotto per ID
    @Query("DELETE FROM foto_prodotti WHERE id_foto_prodotto = :id")
    suspend fun deleteProductImageById(id: String)

    // Elimina tutte le immagini di prodotto
    @Query("DELETE FROM foto_prodotti")
    suspend fun deleteAllProductImages()
}
