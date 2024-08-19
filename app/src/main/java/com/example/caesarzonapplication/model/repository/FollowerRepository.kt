package com.example.caesarzonapplication.model.repository

import androidx.lifecycle.LiveData
import com.example.caesarzonapplication.model.dao.FollowerDao
import com.example.caesarzonapplication.model.entities.Follower

class FollowerRepository (private val followerDao: FollowerDao){

    val readAllData: LiveData<List<Follower>> = followerDao.getAllFollowers()

    suspend fun addFollower(follower: Follower){
        followerDao.addFollower(follower)
    }

    fun getFollowerByUsername(username: String): Follower? {
        return followerDao.getFollowerByUsername(username)
    }

    fun getFollowerById(id: String): Follower? {
        return followerDao.getFollowerById(id)
    }
}