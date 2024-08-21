package com.example.caesarzonapplication.model.dao.notificationDao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.caesarzonapplication.model.entities.notificationEntity.AdminNotification


@Dao
interface AdminNotificationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAdminNotification(adminNotification: AdminNotification)

    @Query("SELECT * FROM notifiche_admin")
    suspend fun getAllAdminNotifications(): List<AdminNotification>


    @Query("DELETE FROM notifiche_admin WHERE id_segnalazione = :reportId")
    suspend fun deleteAdminReportById(reportId: Long)



    @Query("DELETE FROM notifiche_admin WHERE id_richiesta_di_supporto = :supportId")
    suspend fun deleteAdminSupportById(supportId: Long)

    @Query("DELETE FROM notifiche_admin")
     fun deleteAllAdminNotifications()


}