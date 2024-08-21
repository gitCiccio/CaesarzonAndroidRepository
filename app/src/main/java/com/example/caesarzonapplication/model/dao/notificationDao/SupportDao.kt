package com.example.caesarzonapplication.model.dao.notificationDao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.caesarzonapplication.model.entities.notificationEntity.Support

@Dao
interface SupportDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(support: Support)

    @Query("SELECT * FROM richiesta_supporto WHERE id = :id")
    suspend fun getSupportById(id: Long): Support?

    @Query("SELECT * FROM richiesta_supporto")
    suspend fun getAllSupport(): List<Support>

    @Query("DELETE FROM richiesta_supporto WHERE id = :id")
    suspend fun deleteSupportById(id: Long)

    @Query("DELETE FROM richiesta_supporto")
    fun deleteAllSupport()

}