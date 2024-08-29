package com.example.caesarzonapplication.model.viewmodels.userViewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.dto.AddressDTO
import com.example.caesarzonapplication.model.dto.CardDTO
import com.example.caesarzonapplication.model.entities.userEntity.Address
import com.example.caesarzonapplication.model.entities.userEntity.CityData
import com.example.caesarzonapplication.model.repository.userRepository.AddressRepository
import com.example.caesarzonapplication.model.repository.userRepository.CardRepository
import com.example.caesarzonapplication.model.repository.userRepository.CityDataRepository
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URL
import java.util.ArrayList
import java.util.UUID

class CardsViewModel(private val cardRepository: CardRepository): ViewModel() {

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val client = OkHttpClient()
    var cards: ArrayList<CardDTO> = ArrayList()

    lateinit var cardsUuid: List<UUID>

    //caricamento in locale
    fun getAllAddressesAndCityData(){
        cards = cardRepository.getAllCards() as ArrayList<CardDTO>
    }

    //chiamata al server per ricevere gli indirizzi
    fun getUuidCardsFromServer(){
        val manageUrl = URL("http://25.49.50.144:8090/user-api/cards")
        val request =  Request.Builder().url(manageUrl).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        CoroutineScope(Dispatchers.IO).launch {
            try{
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                    return@launch
                }

                println("Risposta dal server: $responseBody")
                val gson = Gson()
                //serve per deserializzare la stringa JSON in una lista di oggetti Address
                val listType = object :  TypeToken<List<UUID>>() {}.type
                cardsUuid = gson.fromJson(responseBody, listType)
                println("Indirizzi recuperati con successo: ${cards.size}")

            }catch (e: Exception){
                e.printStackTrace()
            }

        }

    }

    fun getCardsFromServer(cardsUuid: List<UUID>) {
        CoroutineScope(Dispatchers.IO).launch {
            _isLoading.value = true
            for (uuid in cardsUuid) {
                val manageUrl = URL("http://25.49.50.144:8090/user-api/card/$uuid")
                val request = Request.Builder()
                    .url(manageUrl)
                    .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
                    .build()

                try {
                    val response = client.newCall(request).execute()
                    val responseBody = response.body?.string()

                    if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                        println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                        continue
                    }

                    println("Risposta dal server: $responseBody")
                    val gson = Gson()
                    val valType = object : TypeToken<CardDTO>() {}.type
                    val card = gson.fromJson<CardDTO>(responseBody, valType)

                    cards.add(card)
                    cardRepository.addCard(card)
                    println("Indirizzi recuperati con successo: ${cards.size}")

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                _isLoading.value = false
            }
        }
    }

    //Funzione per eliminare indirizzo
    fun deleteCard(card: CardDTO){
        CoroutineScope(Dispatchers.IO).launch {
            _isLoading.value = true
            doDeleteCard(card)
            _isLoading.value = false
        }
    }

    suspend fun doDeleteCard(card: CardDTO) {

        val manageUrl = URL("http://25.49.50.144:8090/user-api/card/${card.id}")
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
            _isLoading.value = false
        }
    }
    //funzione per aggiungere la carta funziona
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
                //val gson = Gson()
                cards.add(card)//poi quando ricarico i dati lo dovrebbe aggiungere con i dati completi
                cardRepository.addCard(card)//Aggiunge l'indirizzo al db in locale
                println("Indirizzo aggiunto con successo")
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
