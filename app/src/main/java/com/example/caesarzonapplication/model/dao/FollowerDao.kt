package com.example.caesarzonapplication.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.caesarzonapplication.model.entities.Follower

@Dao
interface FollowerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFollower(follower: Follower)

    @Query("SELECT * FROM followers ORDER BY username ASC")
    fun getAllFollowers(): LiveData<List<Follower>>

    @Query("SELECT * FROM followers WHERE username = :username")
    fun getFollowerByUsername(username: String): Follower?

    @Query("SELECT * FROM followers WHERE id = :id")
    fun getFollowerById(id: String): Follower?

}