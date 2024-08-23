package com.example.caesarzonapplication.model.dao.userDao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.caesarzonapplication.model.entities.userEntity.Follower

@Dao
interface FollowerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFollower(follower: Follower)

    @Query("SELECT * FROM follower")
    fun getAllFollowers(): LiveData<List<Follower>>


    @Query("DELETE FROM follower WHERE id = :id")
    suspend fun deleteFollowerById(id: Long)

    @Query("DELETE FROM follower")
    fun deleteAllFollowers()

}