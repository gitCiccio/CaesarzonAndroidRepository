package com.example.caesarzonapplication.model.viewmodels.userViewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.dto.userDTOS.CardDTO
import com.example.caesarzonapplication.model.repository.userRepository.CardRepository
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URL
import java.util.UUID

class CardsViewModel(private val cardRepository: CardRepository): ViewModel() {

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val client = OkHttpClient()
    var cardsUuid: List<UUID> = emptyList()

    private val _cards: MutableStateFlow<List<CardDTO>> = MutableStateFlow(emptyList())
    val cards: StateFlow<List<CardDTO>> = _cards

    fun resetCards() {
        _cards.value = emptyList()
    }


    fun loadCards() {
        viewModelScope.launch {
            resetCards()
            _isLoading.value = true
            try {
                getUuidCardsFromServer()
                val cardsList = mutableListOf<CardDTO>()
                cardsUuid.forEach { uuid ->
                    val card = getCardFromServer(uuid)
                    if (card != null) {
                        cardsList.add(card)
                    }
                }
                _cards.value = cardsList // Assegna l'intera lista una volta completata
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }



    suspend fun getUuidCardsFromServer() {
        val manageUrl = URL("http://25.49.50.144:8090/user-api/cards")
        val request = Request.Builder()
            .url(manageUrl)
            .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
            .build()

        withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                    return@withContext
                }

                println("Risposta dal server: $responseBody")

                val gson = Gson()
                val listType = object : TypeToken<List<UUID>>() {}.type
                cardsUuid = gson.fromJson(responseBody, listType)
            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    suspend fun getCardFromServer(cardUuid: UUID): CardDTO? {
        val manageUrl = URL("http://25.49.50.144:8090/user-api/card?card_id=$cardUuid")
        val request = Request.Builder()
            .url(manageUrl)
            .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
            .build()

        return withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                    return@withContext null
                }

                println("Risposta dal server: $responseBody")
                val gson = Gson()
                val card = gson.fromJson(responseBody, CardDTO::class.java)
                card.id = cardUuid.toString()
                card
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }



    fun deleteCard(card: CardDTO){
        CoroutineScope(Dispatchers.IO).launch {
            _isLoading.value = true
            doDeleteCard(card)
            _isLoading.value = false
        }
    }

    suspend fun doDeleteCard(card: CardDTO) {

        val manageUrl = URL("http://25.49.50.144:8090/user-api/card?card_id=${card.id}")
        val request = Request.Builder().url(manageUrl).delete().addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        try{
            withContext(Dispatchers.IO){
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                }

                println("Risposta dal server: $responseBody")

                cardRepository.deleteCardById(card.id)
                _cards.value -= card
                println("Carta eliminato con successo")
            }
        }catch (e: Exception){
            e.printStackTrace()
            println("Errore durante la chiamata: ${e.message}")
        }
    }


    fun addCard(card: CardDTO){
        viewModelScope.launch {
            _isLoading.value = true
            try{
                doAddCard(card)
            }catch (e: Exception){
                e.printStackTrace()
            }
            finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun doAddCard(card: CardDTO){
        val manageUrl = URL("http://25.49.50.144:8090/user-api/card")
        val JSON = "application/json; charset=utf-8".toMediaType()

        val gson = Gson()
        val json = gson.toJson(card)
        val requestBody = json.toRequestBody(JSON)
        val request = Request.Builder().url(manageUrl).post(requestBody).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO){
            try{
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                }

                println("Risposta dal server: $responseBody")
                _cards.value += card
                cardRepository.addCard(card)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}

class CardsViewModelFactory(
    private val cardRepository: CardRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardsViewModel::class.java)) {
            return CardsViewModel(cardRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
