package com.example.caesarzonapplication.model.dao.notificationDao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.caesarzonapplication.model.entities.notificationEntity.UserNotification

@Dao
interface UserNotificationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userNotification: UserNotification)

    @Query("SELECT * FROM notifiche_utente")
    suspend fun getAllUserNotifications(): List<UserNotification>

    @Query("DELETE FROM notifiche_utente WHERE id = :id")
    suspend fun deleteUserNotificationById(id: Long)

    @Query("DELETE FROM notifiche_utente")
    fun deleteAllUserNotifications()

}