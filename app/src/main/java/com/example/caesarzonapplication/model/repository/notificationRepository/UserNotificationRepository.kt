package com.example.caesarzonapplication.model.repository.notificationRepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.caesarzonapplication.model.dao.notificationDao.UserNotificationDao
import com.example.caesarzonapplication.model.entities.notificationEntity.UserNotification

class UserNotificationRepository(private val userNotificationDao: UserNotificationDao) {

    suspend fun addUserNotification(userNotification: UserNotification): Boolean {
        return try {
            userNotificationDao.addUserNotification(userNotification)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getAllUserNotifications(): LiveData<List<UserNotification>> {
        return try {
            val notifications = userNotificationDao.getAllUserNotifications()
            notifications
        } catch (e: Exception) {
            e.printStackTrace()
            MutableLiveData(emptyList())
        }
    }

    suspend fun deleteUserNotificationById(id: Long): Boolean {
        return try {
            userNotificationDao.deleteUserNotificationById(id)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteAllUserNotifications(): Boolean {
        return try {
            userNotificationDao.deleteAllUserNotifications()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

