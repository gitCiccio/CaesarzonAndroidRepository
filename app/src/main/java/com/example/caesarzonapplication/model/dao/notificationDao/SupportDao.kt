package com.example.caesarzonapplication.model.dao.notificationDao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.caesarzonapplication.model.entities.notificationEntity.Support

@Dao
interface SupportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(support: Support)


    @Query("SELECT * FROM richiesta_supporto")
    fun getAllSupport(): List<Support>

    @Query("SELECT * FROM richiesta_supporto WHERE id_richiesta_di_supporto = :id")
    fun getById(id: String): Support


    @Query("DELETE FROM richiesta_supporto WHERE id_richiesta_di_supporto = :id")
    suspend fun deleteSupportById(id: String)

    @Query("DELETE FROM richiesta_supporto")
    fun deleteAllSupport()

}
