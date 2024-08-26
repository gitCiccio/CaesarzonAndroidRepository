package com.example.caesarzonapplication.model.viewmodels.userViewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.dto.AddressDTO
import com.example.caesarzonapplication.model.dto.CityDataDTO
import com.example.caesarzonapplication.model.entities.userEntity.Address
import com.example.caesarzonapplication.model.entities.userEntity.CityData
import com.example.caesarzonapplication.model.repository.userRepository.AddressRepository
import com.example.caesarzonapplication.model.repository.userRepository.CityDataRepository
import com.example.caesarzonapplication.model.repository.userRepository.UserRepository
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

class AddressViewModel(private val addressRepository: AddressRepository, private val cityDataRepository: CityDataRepository): ViewModel() {

    private val client = OkHttpClient()
    var addresses: ArrayList<AddressDTO> = ArrayList()
    //var cityData: ArrayList<CityDataDTO> = ArrayList() forse non serve

     var addressesUuid: List<UUID> = emptyList()


    init{
        getAllAddressesAndCityData()
    }
    //caricamento in locale
    fun getAllAddressesAndCityData(){
        getUuidAddressesFromServer()
        getAddressesFromServer(addressesUuid)//da capire se server
    }

    //chiamata al server per ricevere gli indirizzi
    fun getUuidAddressesFromServer(){
        val manageUrl = URL("http://25.49.50.144:8090/user-api/address")
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
                addressesUuid = gson.fromJson(responseBody, listType)
                println("Indirizzi recuperati con successo: ${addressesUuid.size}")

            }catch (e: Exception){
                e.printStackTrace()
            }

        }

    }

    fun getAddressesFromServer(addressesUuid: List<UUID>) {
        CoroutineScope(Dispatchers.IO).launch {

            for (uuid in addressesUuid) {
                val manageUrl = URL("http://25.49.50.144:8090/user-api/address/$uuid")
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
                    val valType = object : TypeToken<Address>() {}.type
                    val address = gson.fromJson<AddressDTO>(responseBody, valType)

                    addresses.add(address)
                    addressRepository.addAddress(address)
                    println("Indirizzi recuperati con successo: ${addresses.size}")

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    //Funzione per eliminare indirizzo
    fun deleteAddress(address: AddressDTO){
        CoroutineScope(Dispatchers.IO).launch {
            doDeleteAddress(address)
        }
    }

    suspend fun doDeleteAddress(address: AddressDTO) {

        val manageUrl = URL("http://25.49.50.144:8090/user-api/address/${address.id}")
        val request = Request.Builder().url(manageUrl).delete().addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        try{
            withContext(Dispatchers.IO){
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                }

                println("Risposta dal server: $responseBody")
                addressRepository.deleteAddressByCityId(address)
                println("Indirizzo eliminato con successo")
            }
        }catch (e: Exception){
            e.printStackTrace()
            println("Errore durante la chiamata: ${e.message}")
        }
    }

    fun addAddress(address: AddressDTO){
        viewModelScope.launch {
            try{
                doAddAddress(address)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun doAddAddress(address: AddressDTO){
        println("so in doAddAddress")
        val manageUrl = URL("http://25.49.50.144:8090/user-api/address")
        val JSON = "application/json; charset=utf-8".toMediaType()

        val gson = Gson()
        val json = gson.toJson(address)
        val requestBody = json.toRequestBody(JSON)
        val request = Request.Builder().url(manageUrl).post(requestBody).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
        println("valore del token: ${myToken?.accessToken}")
        withContext(Dispatchers.IO){
            println("sono nel withContext della doAddress")
            try{
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                println("response code: ${response.code}")
                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                }

                println("Risposta dal server: $responseBody")
                //val gson = Gson()
                addresses.add(address)//poi quando ricarico i dati lo dovrebbe aggiungere con i dati completi
                addressRepository.addAddress(address)//Aggiunge l'indirizzo al db in locale
                println("Indirizzo aggiunto con successo")
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
    //riesco a prendere i suggerimenti di città
     fun getCityTip(cityTip: String) {
        val manageUrl = URL("http://25.49.50.144:8090/user-api/city?sugg=$cityTip")
        val request = Request.Builder()
            .url(manageUrl)
            .addHeader("Authorization", "Bearer ${myToken?.accessToken ?: ""}") // Assicurati che il token non sia nullo
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                    //return@launch emptyList<String>()
                }

                println("Risposta dal server: $responseBody")

                // Utilizza Gson per convertire la risposta JSON in una lista di Stringhe
                val gson = Gson()
                val listType = object : TypeToken<List<String>>() {}.type
                val cityList: List<String> = gson.fromJson(responseBody, listType)

                // Stampa ogni città ottenuta
                cityList.forEach { city ->
                    println(city)
                }

                //return@withContext cityList
            } catch (e: Exception) {
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
                //return@withContext emptyList<String>()
            }
        }
    }

    //Deve tornare un city data dto con tutte le informazioni
    fun getFullCityData(cityName: String){
        val manageUrl = URL("http://25.49.50.144:8090/user-api/city-data?city=$cityName")
        val request = Request.Builder()
            .url(manageUrl)
            .addHeader("Authorization", "Bearer ${myToken?.accessToken ?: ""}") // Assicurati che il token non sia nullo
            .build()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                    //return@launch emptyList<String>()
                }

                println("Risposta dal server: $responseBody")

                // Utilizza Gson per convertire la risposta JSON in una lista di Stringhe
                val gson = Gson()
                val listType = object : TypeToken<List<CityDataDTO>>() {}.type
                val cityList: List<String> = gson.fromJson(responseBody, listType)

                // Stampa ogni città ottenuta
                cityList.forEach { city ->
                    println(city)
                }

                //return@withContext cityList
            } catch (e: Exception) {
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
                //return@withContext emptyList<String>()
            }
        }
    }
}

class AddressViewModelFactory(
    private val addressRepository: AddressRepository,
    private val cityDataRepository: CityDataRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddressViewModel::class.java)) {
            return AddressViewModel(addressRepository, cityDataRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}