package com.example.caesarzonapplication.model.repository.notificationRepository

import com.example.caesarzonapplication.model.dao.notificationDao.AdminNotificationDao
import com.example.caesarzonapplication.model.entities.notificationEntity.AdminNotification
import com.example.caesarzonapplication.model.entities.notificationEntity.Support

class AdminNotificationRepository(private val adminNotificationDao: AdminNotificationDao) {

    suspend fun deleteByReportId(id: Long): Boolean {
        return try {
            adminNotificationDao.deleteAdminReportById(id)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteBySupport(support: Support): Boolean{
        return try {
            adminNotificationDao.deleteAdminSupportById(support.id)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun addNotification(adminNotification: AdminNotification): Boolean {
        return try {
            adminNotificationDao.addAdminNotification(adminNotification)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false        }
    }
}

