package com.example.caesarzonapplication.model.repository.userRepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.caesarzonapplication.model.dao.userDao.CardDao
import com.example.caesarzonapplication.model.entities.userEntity.Card

class CardRepository(private val cardDao: CardDao) {

    suspend fun addCard(card: Card): Boolean {
        return try {
            cardDao.addCard(card)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getAllCards(): LiveData<List<Card>> {
        return try {
            val cards = cardDao.getAllCards()
            cards
        } catch (e: Exception) {
            e.printStackTrace()
            MutableLiveData(emptyList())
        }
    }


    suspend fun deleteCardById(id: Long): Boolean {
        return try {
            cardDao.deleteCardById(id)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun deleteAllCards(): Boolean {
        return try {
            cardDao.deleteAllCards()
            true
        } catch (e: Exception) {
            false
        }
    }
}

