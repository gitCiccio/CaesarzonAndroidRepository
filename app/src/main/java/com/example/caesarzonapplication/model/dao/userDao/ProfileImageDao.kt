package com.example.caesarzonapplication.model.dao.userDao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.caesarzonapplication.model.entities.userEntity.ProfileImage

@Dao
interface ProfileImageDao {

    // Inserisce una nuova immagine profilo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfileImage(profileImage: ProfileImage)

    // Recupera un'immagine profilo per ID
    @Query("SELECT * FROM foto_utente")
    fun getProfileImage(): LiveData<ProfileImage?>


    // Elimina un'immagine profilo per ID
    @Query("DELETE FROM foto_utente WHERE id = :id")
    suspend fun deleteProfileImageById(id: Long)

    // Elimina tutte le immagini profilo
    @Query("DELETE FROM foto_utente")
    suspend fun deleteAllProfileImages()
}
