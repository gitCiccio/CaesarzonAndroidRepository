package com.example.caesarzonapplication.model.repository.userRepository

import com.example.caesarzonapplication.model.dao.userDao.UserDao
import com.example.caesarzonapplication.model.dto.userDTOS.UserDTO
import com.example.caesarzonapplication.model.dto.authDTOS.UserRegistrationDTO
import com.example.caesarzonapplication.model.entities.userEntity.User

class UserRepository(private val userDao: UserDao) {

    // Inserisci un nuovo utente
    suspend fun addUser(user: UserRegistrationDTO): Boolean {
        return try {
            userDao.addUser(User(user.lastName, user.username, user.firstName, "", user.email, user.credentialValue, ""))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun updateUser(user: UserDTO): Boolean {
        return try {
            val oldUSer = userDao.getUserData(username = user.username)
            userDao.addUser(User(user.lastName, user.username, user.firstName, "", user.email, oldUSer.credentialValue, ""))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }



    // Recupera i dati dell'utente
    suspend fun getUserData(username: String): UserDTO {
        val normalizedUsername = username.lowercase() // Converti l'username in minuscolo
        val user = userDao.getUserData(normalizedUsername)
        return UserDTO(user.username, user.firstName, user.lastName, user.phoneNumber, user.email)
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

