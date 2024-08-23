package com.example.caesarzonapplication.model.repository.userRepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

     fun getAllFollowers(): LiveData<List<Follower>> {
        return try {
            followerDao.getAllFollowers()
        } catch (e: Exception) {
            e.printStackTrace()
            MutableLiveData(emptyList())
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

