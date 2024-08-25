package com.example.caesarzonapplication.model.repository.userRepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.caesarzonapplication.model.dao.userDao.CardDao
import com.example.caesarzonapplication.model.dto.CardDTO
import com.example.caesarzonapplication.model.entities.userEntity.Card

class CardRepository(private val cardDao: CardDao) {

    suspend fun addCard(card: CardDTO): Boolean {
        return try {
            val card = Card(id = 0, id_carta = card.id, cardNumber = card.cardNumber, owner = card.owner, cvv = card.cvv, expirationDate = card.expirationDate, balance = card.balance)
            cardDao.addCard(card)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getAllCards(): LiveData<List<CardDTO>> {
        return try {
            val cards = cardDao.getAllCards()
            val cardsDTO = mutableListOf<CardDTO>()
            for(card in cards){
                cardsDTO.add(CardDTO(card.id_carta, card.cardNumber, card.owner, card.cvv, card.expirationDate, card.balance))
            }
            return MutableLiveData(cardsDTO)
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

    suspend fun deleteCardByUuid(id: String): Boolean {
        return try {
            cardDao.deleteCardByUuid(id)
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

