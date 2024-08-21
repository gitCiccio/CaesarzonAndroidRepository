package com.example.caesarzonapplication.model.repository.userRepository

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

    suspend fun getAllCards(): List<Card> {
        return try {
            val cards = cardDao.getAllCards()
            cards
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getCardById(id: Long): Card? {
        return try {
            val card = cardDao.getCardById(id)
            card
        } catch (e: Exception) {
            e.printStackTrace()
            null
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

    suspend fun deleteAllCards(): Boolean {
        return try {
            cardDao.deleteAllCards()
            true
        } catch (e: Exception) {
            false
        }
    }
}

