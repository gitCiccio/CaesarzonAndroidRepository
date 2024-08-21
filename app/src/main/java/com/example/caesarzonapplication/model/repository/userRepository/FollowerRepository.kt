package com.example.caesarzonapplication.model.repository.userRepository

import com.example.caesarzonapplication.model.dao.userDao.FollowerDao
import com.example.caesarzonapplication.model.entities.userEntity.Follower

class FollowerRepository(private val followerDao: FollowerDao) {

    suspend fun addFollower(follower: Follower): Boolean{
        return try {
            followerDao.addFollower(follower)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getAllFollowers(): List<Follower> {
        return try {
            val followers = followerDao.getAllFollowers()
            followers
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getFollowerById(id: Long): Follower? {
        return try {
            val follower = followerDao.getFollowerById(id)
            follower
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun deleteFollowerById(id: Long): Boolean {
        return try {
            followerDao.deleteFollowerById(id)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteAllFollowers(): Boolean {
        return try {
            followerDao.deleteAllFollowers()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

