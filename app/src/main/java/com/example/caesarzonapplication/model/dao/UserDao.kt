package com.example.caesarzonapplication.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.caesarzonapplication.model.entities.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM utenti ORDER BY id ASC")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT * FROM utenti WHERE username = :username")
    fun getUserByUsername(username: String): User?

    @Query("SELECT * FROM utenti WHERE id = :id")
    fun getUserById(id: String): User?

}