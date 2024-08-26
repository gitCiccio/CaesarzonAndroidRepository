package com.example.caesarzonapplication.model.dao.notificationDao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.caesarzonapplication.model.entities.notificationEntity.UserNotification

@Dao
interface UserNotificationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUserNotification(userNotification: UserNotification)

    @Query("SELECT * FROM notifiche_utente")
    fun getAllUserNotifications(): LiveData<List<UserNotification>>

    @Query("DELETE FROM notifiche_utente WHERE id_notifiche_utente = :id")
    suspend fun deleteUserNotificationById(id: String)

    @Query("DELETE FROM notifiche_utente")
    fun deleteAllUserNotifications()

}