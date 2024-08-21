package com.example.caesarzonapplication.model.repository.notificationRepository

import com.example.caesarzonapplication.model.dao.notificationDao.SupportDao
import com.example.caesarzonapplication.model.entities.notificationEntity.Support
class SupportRepository(private val supportDao: SupportDao) {

    suspend fun addSupport(support: Support): Boolean{
        return try {
            supportDao.insert(support)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getSupportById(id: Long): Support? {
        return try {
            val support = supportDao.getSupportById(id)
            support
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getAllSupport(): List<Support> {
        return try {
            val supports = supportDao.getAllSupport()
            supports
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun deleteSupportById(id: Long): Boolean {
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

