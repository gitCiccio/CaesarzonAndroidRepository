package com.example.caesarzonapplication.model.dao.notificationDao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.caesarzonapplication.model.entities.notificationEntity.Ban

@Dao
interface BanDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBan(ban: Ban)

    @Query("SELECT * FROM Ban")
    fun getAllBans(): LiveData<List<Ban>>

    @Query("DELETE FROM Ban WHERE username_utente = :username")
    suspend fun deleteBanByUserUsername(username: String)

    @Query("DELETE FROM Ban")
    fun deleteAllBans()

}