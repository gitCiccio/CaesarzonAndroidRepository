package com.example.caesarzonapplication.model.dao.notificationDao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.caesarzonapplication.model.entities.notificationEntity.Support

@Dao
interface SupportDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSupport(support: Support)


    @Query("SELECT * FROM richiesta_supporto")
    fun getAllSupport(): LiveData<List<Support>>

    @Query("DELETE FROM richiesta_supporto WHERE id = :id")
    suspend fun deleteSupportById(id: Long)

    @Query("DELETE FROM richiesta_supporto")
    fun deleteAllSupport()

}