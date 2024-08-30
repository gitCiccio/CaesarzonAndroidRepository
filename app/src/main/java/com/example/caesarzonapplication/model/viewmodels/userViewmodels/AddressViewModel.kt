package com.example.caesarzonapplication.model.viewmodels.userViewmodels

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.dto.userDTOS.AddressDTO
import com.example.caesarzonapplication.model.dto.userDTOS.CityDataDTO
import com.example.caesarzonapplication.model.entities.userEntity.Address
import com.example.caesarzonapplication.model.repository.userRepository.AddressRepository
import com.example.caesarzonapplication.model.repository.userRepository.CityDataRepository
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

class AddressViewModel(private val addressRepository: AddressRepository, private val cityDataRepository: CityDataRepository): ViewModel() {

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val client = OkHttpClient()

    private val _addresses: MutableStateFlow<List<AddressDTO>> = MutableStateFlow(emptyList())
    val addresses: StateFlow<List<AddressDTO>> = _addresses

    private val _cityData = MutableLiveData<List<String>>()
    val cityData: LiveData<List<String>> = _cityData

    var addressesUuid: List<UUID> = emptyList()

    var selectedCityId: String = ""

    private val _cityDataDTO = MutableLiveData<CityDataDTO?>(null)
    val cityDataDTO: LiveData<CityDataDTO?> = _cityDataDTO

    val gson = Gson()

    fun resetAddresses() {
        _addresses.value = emptyList()
    }
    fun loadAddresses() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                getUuidAddressesFromServer()
                addressesUuid.forEach {
                    getAddressesFromServer(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    //chiamata al server per ricevere gli indirizzi
    fun getUuidAddressesFromServer(){
        val manageUrl = URL("http://25.49.50.144:8090/user-api/addresses")
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
                val listType = object :  TypeToken<List<UUID>>() {}.type
                addressesUuid = gson.fromJson(responseBody, listType)
                println("Indirizzi recuperati con successo: ${addressesUuid.size}")

            }catch (e: Exception){
                e.printStackTrace()
            }

        }

    }

    suspend fun getAddressesFromServer(addressUuid: UUID) {
        val manageUrl = URL("http://25.49.50.144:8090/user-api/address?address_id=$addressUuid")
        val request = Request.Builder()
            .url(manageUrl)
            .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
            .build()

        withContext(Dispatchers.IO) {
            _isLoading.value = true
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                    return@withContext
                }

                println("Risposta dal server: $responseBody")
                val gson = Gson()
                val address = gson.fromJson(responseBody, AddressDTO::class.java)
                address.id = addressUuid.toString()

                withContext(Dispatchers.Main) {
                    _addresses.value += address
                }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
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

        val manageUrl = URL("http://25.49.50.144:8090/user-api/address?address_id=${address.id}")
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
                _addresses.value -= address
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
        val manageUrl = URL("http://25.1.9.69:8090/user-api/address")
        val JSON = "application/json; charset=utf-8".toMediaType()

        val json = gson.toJson(address)
        val requestBody = json.toRequestBody(JSON)
        val request = Request.Builder().url(manageUrl).post(requestBody).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
        withContext(Dispatchers.IO){
            try{
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                println("response code: ${response.code}")
                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                }

                println("Risposta dal server: $responseBody")
                _addresses.value += address
                addressRepository.addAddress(address)
                println("Indirizzo aggiunto con successo")
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
    //riesco a prendere i suggerimenti di città
     fun getCityTip(cityTip: String){
        val manageUrl = URL("http://25.49.50.144:8090/user-api/city?sugg=$cityTip")
        val request = Request.Builder()
            .url(manageUrl)
            .addHeader("Authorization", "Bearer ${myToken?.accessToken}") // Assicurati che il token non sia nullo
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                    return@launch
                }

                println("Risposta dal server: $responseBody")

                // Utilizza Gson per convertire la risposta JSON in una lista di Stringhe
                val listType = object : TypeToken<List<String>>() {}.type
                val cityList = gson.fromJson<List<String>>(responseBody, listType)

                _cityData.postValue(cityList)
            } catch (e: Exception) {
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
                return@launch
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
                    return@launch
                }

                println("Risposta dal server: $responseBody")

                val listType = object : TypeToken<CityDataDTO>() {}.type
                val cityData = gson.fromJson<CityDataDTO>(responseBody, listType)

                selectedCityId = cityData.id.toString()

                _cityDataDTO.postValue(cityData)
            } catch (e: Exception) {
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
                return@launch
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