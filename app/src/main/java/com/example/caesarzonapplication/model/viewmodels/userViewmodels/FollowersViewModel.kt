package com.example.caesarzonapplication.model.viewmodels.userViewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.dto.FollowerDTO
import com.example.caesarzonapplication.model.dto.UserSearchDTO
import com.example.caesarzonapplication.model.entities.userEntity.Follower
import com.example.caesarzonapplication.model.repository.userRepository.FollowerRepository
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import java.net.URL


//vedere come aggiungere i follower
//capire perché i dati non sono salvati subito
//capire perché non cambia lo stato
class FollowersViewModel(private val followerRepository: FollowerRepository): ViewModel() {

    private lateinit var getAllFollowers: LiveData<List<Follower>>

    val client = OkHttpClient()
    val gson = Gson()

    init {
        println("Sono nell'init")
        viewModelScope.launch{
            getAllFollowers = followerRepository.getAllFollowers()

        }
    }

    private var _users = mutableStateListOf<UserSearchDTO>()

    //User inviati da aldo, quando li cerco
    val users: List<UserSearchDTO> get() = _users

    //FollowerDTO, gli utenti che aldo mi manda al caricamento della pagina
    private var _followers = mutableStateListOf<UserSearchDTO>()
    val followers: List<UserSearchDTO> get() = _followers

    private var _friends = mutableStateListOf<UserSearchDTO>()
    val friends: List<UserSearchDTO> get() = _friends

    private val _newFollowersAndFriends = mutableListOf<UserSearchDTO>()
    //capire come fare
    //private val _deletedFollowersAndFriends = mutableListOf<FollowerDTO>()
    //Lista degli amici che mi gestisco dopo che ho i follower


    //Aggiunta del follower ok
    fun addFollower(follower: UserSearchDTO) {
        // Update the state of the users who become followers
        val user = _users.find { it.username == follower.username }
        user?.let {
            it.follower = true

            val existingFollower = _followers.find { it.username == follower.username }
            if (existingFollower == null) {
                _followers.add(it)
                _newFollowersAndFriends.add(it)
            }
        }
        viewModelScope.launch {
            doAddFollower(follower)
        }
    }

    //Da provare domani
    suspend fun doAddFollower(follower: UserSearchDTO){
        val manageURL = URL("http://25.49.50.144:8090/user-api/followers")
        val JSON = "application/json; charset=utf-8".toMediaType()

        val gson = Gson()
        val json = gson.toJson(listOf(follower))
        val requestBody = json.toRequestBody(JSON)
        val request = Request.Builder().url(manageURL).post(requestBody).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO){
            println("Snono nel try dell'aggiunta del follower")
            try{
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                println("response code: ${response.code}")
                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                }
                println("Risposta dal server: $responseBody")
                println("follower Aggiunto con successo")
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }



    fun removeFollower(follower: UserSearchDTO) {
        // Trova l'utente con lo stesso username del follower
        val user = _users.find { it.username == follower.username }
        if (user != null) {
            println("Username: ${user.username}, status follower: ${user.follower}, status friend: ${user.friend}")
        }
        // Se l'utente è trovato, esegui le operazioni richieste
        user?.let {
            // Controlla e aggiorna il friend status se necessario
            if (it.friend) {
                println("Username: ${user.username}, status follower: ${user.follower}, status friend: ${user.friend}")
                it.friend = false
                _friends.removeIf { friend -> friend.username == it.username }
            }

            // Aggiorna il follower status
            it.follower = false
            println("Username: ${user.username}, new status follower: ${user.follower}, new status friend: ${user.friend}")
            _followers.removeIf { follower -> follower.username == it.username }

            // Aggiungi l'utente aggiornato a _newFollowersAndFriends
            _newFollowersAndFriends.add(it)
        }
        viewModelScope.launch {
            doRemoveFollower(follower)
        }
    }

    suspend fun doRemoveFollower(follower: UserSearchDTO){
        val manageURL = URL("http://25.49.50.144:8090/user-api/followers/${follower.username}")
        val request = Request.Builder().url(manageURL).delete().addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
        withContext(Dispatchers.IO){
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                }
                println("Risposta dal server: $responseBody")
                println("Follower eliminato con successo")
                followerRepository.deleteFollowerByUsername(follower.username)
            }catch (e: Exception){
                e.printStackTrace()
                println("Errore nell'eliminazione del follower")
            }
        }
    }

    fun toggleFriendStatus(follower: UserSearchDTO) {
        println("Inside toggleFriendStatus")
        println("Username: ${follower.username}, status follower: ${follower.follower}, status friend: ${follower.friend}")

        // Trova l'utente nella lista _users e aggiorna direttamente l'oggetto trovato
        val user = _users.find { it.username == follower.username }
        user?.let {
            // Inverti lo stato di friendStatus
            it.friend = !it.friend
            println("Username: ${it.username}, status follower: ${it.follower}, status friend: ${it.friend}")

            // Aggiorna la lista _friends in base al nuovo stato di friendStatus
            if (it.friend) {
                if (!_friends.contains(it)) {
                    _friends.add(it)
                }
            } else {
                _friends.remove(it)
            }

            // Aggiorna la lista _newFollowersAndFriends
            val existingIndex = _newFollowersAndFriends.indexOfFirst { nf -> nf.username == it.username }
            if (existingIndex >= 0) {
                _newFollowersAndFriends[existingIndex] = it
            } else {
                _newFollowersAndFriends.add(it)
            }

            // Chiama la funzione per aggiornare lo stato nel server
            updateFollowerStatus(_newFollowersAndFriends)
        }
    }

    //da vedere domani
    fun searchUsers(username: String) {
        _users.clear()
        if (username.isEmpty()) return

        val manageURL = URL("http://25.49.50.144:8090/user-api/users/$username")
        val request = Request.Builder().url(manageURL).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
        try {

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if(!response.isSuccessful){
                println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
            }

            val listUsername = object : TypeToken<List<String>>() {}.type
            for (user in gson.fromJson<List<String>>(responseBody, listUsername)) {
                val userSearchDto = UserSearchDTO(user,  "",false, false)
                _users.add(userSearchDto)
            }

        }
        catch (e: Exception) {
            println("Error: ${e.message}")
        }

    }

    fun loadAllFollowers()
    {
        viewModelScope.launch {
            doLoadFollowersAndFriends(0, false)
        }
    }

    fun loadAllFriends()
    {
        viewModelScope.launch {
            doLoadFollowersAndFriends(0, true)
        }
    }

    fun updateFollowerStatus(followersAndFriends: List<UserSearchDTO>){
        viewModelScope.launch {
            doUpdateFollowerStatus(followersAndFriends)
        }
    }
    //Inserire nell'init per il caricamento dei dati
    //TODO GET PEr ottenere la lista di username dei follower, booleana per indicare se un follower è amico o meno e immagine di profilo, sarà una loadDeiFollower
    suspend fun doLoadFollowersAndFriends(flw: Int, friend: Boolean) {
        val manageURL = URL("http://25.49.50.144:8090/user-api/followers?flw=$flw&friend=$friend")
        val request = Request.Builder().url(manageURL).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
        println("funzione per caricare gli utenti follower e amici")
        _friends.clear()
        _followers.clear()

        withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                println("response code: ${response.code}")
                println("Response Body: $responseBody")

                if (!response.isSuccessful) {
                    println("Chiamata fallita o risposta vuota. Codice di stato: ${response.code}")
                } else {
                    val jsonResponse = JSONArray(responseBody)
                    println("Response Body: $jsonResponse")

                    val listUsers = object : TypeToken<List<UserSearchDTO>>() {}.type
                    val users: List<UserSearchDTO> = gson.fromJson(jsonResponse.toString(), listUsers)

                    if (friend) {
                        println("Sto aggiungendo gli amici")
                        _friends.addAll(users)
                    } else {
                        println("Sto aggiungendo i follwer")
                        _followers.addAll(users)
                    }

                    // Foreach per inserire gli utenti usando followerRepository
                    for (user in users) {
                        followerRepository.addFollower(FollowerDTO("","",user.username, user.friend)) // Supponendo che followerRepository abbia un metodo save()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println("Errore nel caricamento dei follower e amici")
            }
        }
    }


    //Fai domani
        //TODO POST per modificare lo stato di un follower, amico o non amico, passiamo username
    suspend fun doUpdateFollowerStatus(followersAndFriends: List<UserSearchDTO>) {
        val manageURL = "http://25.49.50.144:8090/user-api/followers"
        println("checkStatus")
        for (follower in followersAndFriends) {
            println("Username: ${follower.username}, status follower: ${follower.follower}, status friend: ${follower.friend}")
        }
        withContext(Dispatchers.IO) {
            try {
                val jsonInputString = gson.toJson(followersAndFriends) // Serializza la lista in JSON
                println("JSON inviato: $jsonInputString")
                val mediaType = "application/json; charset=utf-8".toMediaType()
                val requestBody = jsonInputString.toRequestBody(mediaType) // Crea il RequestBody

                // Costruisci la richiesta HTTP
                val request = Request.Builder()
                    .url(manageURL)
                    .post(requestBody)
                    .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "application/json")
                    .build()

                // Esegui la richiesta
                val response: Response = client.newCall(request).execute()
                val responseCode = response.code

                // Gestisci la risposta
                if (responseCode == 200) {
                    println("Follower status updated successfully")
                } else {
                    println("Error updating follower status: ${response.message}")
                }

            } catch (e: Exception) {
                println("Error: ${e.message}")
            }
        }
    }

}
class FollowersViewModelFactory(
    private val followerRepository: FollowerRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FollowersViewModel::class.java)) {
            return FollowersViewModel(followerRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}