package com.example.caesarzonapplication.model.repository.userRepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.caesarzonapplication.model.dao.userDao.ProfileImageDao
import com.example.caesarzonapplication.model.entities.userEntity.ProfileImage

class ProfileImageRepository(private val profileImageDao: ProfileImageDao) {

    // Inserisce una nuova immagine profilo
    suspend fun insertProfileImage(profileImage: ProfileImage): Boolean {
        return try {
            profileImageDao.insertProfileImage(profileImage)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Recupera un'immagine profilo per ID
    fun getProfileImage(): LiveData<ProfileImage?> {
        return try {
            profileImageDao.getProfileImage()
        } catch (e: Exception) {
            e.printStackTrace()
            MutableLiveData(null)
        }
    }

    // Elimina un'immagine profilo per ID
    suspend fun deleteProfileImageById(id: String): Boolean {
        return try {
            profileImageDao.deleteProfileImageById(id)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Elimina tutte le immagini profilo
    suspend fun deleteAllProfileImages(): Boolean {
        return try {
            profileImageDao.deleteAllProfileImages()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
