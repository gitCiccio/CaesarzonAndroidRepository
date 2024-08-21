package com.example.caesarzonapplication.model.repository.userRepository

import androidx.lifecycle.LiveData
import com.example.caesarzonapplication.model.dao.userDao.UserDao
import com.example.caesarzonapplication.model.entities.userEntity.User

class UserRepository(private val userDao: UserDao) {

    // Inserisci un nuovo utente
    suspend fun addUser(user: User): Boolean {
        return try {
            userDao.addUser(user)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Recupera i dati dell'utente
    suspend fun getUserData(): User? {
        return try {
            val userData = userDao.getUserData()
            userData
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Elimina i dati dell'utente
    suspend fun deleteUserData(): Boolean {
        return try {
            userDao.deleteUserData()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

