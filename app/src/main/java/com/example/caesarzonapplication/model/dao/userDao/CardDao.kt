package com.example.caesarzonapplication.model.dao.userDao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.caesarzonapplication.model.entities.userEntity.Card

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCard(card: Card)

    @Query("SELECT * FROM carte")
    fun getAllCards(): LiveData<List<Card>>

    @Query("DELETE FROM carte WHERE id = :id")
    suspend fun deleteCardById(id: Long)

    @Query("DELETE FROM carte")
    fun deleteAllCards()
}