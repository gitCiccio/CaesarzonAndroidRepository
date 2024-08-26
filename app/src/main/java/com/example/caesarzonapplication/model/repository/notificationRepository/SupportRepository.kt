package com.example.caesarzonapplication.model.repository.notificationRepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.caesarzonapplication.model.dao.notificationDao.SupportDao
import com.example.caesarzonapplication.model.entities.notificationEntity.Support
class SupportRepository(private val supportDao: SupportDao) {

    suspend fun addSupport(support: Support): Boolean{
        return try {
            supportDao.addSupport(support)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getAllSupport(): LiveData<List<Support>> {
        return try {
            val supports = supportDao.getAllSupport()
            supports
        } catch (e: Exception) {
            e.printStackTrace()
            MutableLiveData(emptyList())
        }
    }

    suspend fun deleteSupportById(id: String): Boolean {
        return try {
            supportDao.deleteSupportById(id)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteAllSupport(): Boolean {
        return try {
            supportDao.deleteAllSupport()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

