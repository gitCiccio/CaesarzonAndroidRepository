package com.example.caesarzonapplication.model.dao.userDao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.caesarzonapplication.model.entities.notificationEntity.Support

@Dao
interface SupportDAO {
    @Query("SELECT * FROM richiesta_supporto")
    fun getAll(): List<Support>


    @Query("SELECT * FROM richiesta_supporto WHERE id_richiesta_di_supporto = :id")
    fun getById(id: String): Support

    @Insert
    suspend fun insert(support: Support): Boolean

    @Query("DELETE FROM richiesta_supporto WHERE id_richiesta_di_supporto = :id")
    suspend fun deleteById(id: String)

}