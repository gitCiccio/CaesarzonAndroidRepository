package com.example.caesarzonapplication.model.viewmodels.userViewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.dto.productDTOS.BasicWishlistDTO
import com.example.caesarzonapplication.model.dto.productDTOS.ChangeVisibilityDTO
import com.example.caesarzonapplication.model.dto.productDTOS.SendWishlistProductDTO
import com.example.caesarzonapplication.model.dto.productDTOS.SingleWishlistProductDTO
import com.example.caesarzonapplication.model.dto.productDTOS.WishProductDTO
import com.example.caesarzonapplication.model.dto.productDTOS.WishlistDTO
import com.example.caesarzonapplication.model.repository.wishlistRepository.WishlistRepository
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.net.URL
import java.util.UUID

class WishlistViewModel(private val wishlistRepository: WishlistRepository): ViewModel(){

    private val client = OkHttpClient()
    private val gson = Gson()


    private val _wishlistsPrivate: MutableStateFlow<List<BasicWishlistDTO>> = MutableStateFlow(emptyList())
    val wishlistsPrivate: StateFlow<List<BasicWishlistDTO>> get() = _wishlistsPrivate

    private val _wishlistsPublic: MutableStateFlow<List<BasicWishlistDTO>> = MutableStateFlow(emptyList())
    val wishlistsPublic: StateFlow<List<BasicWishlistDTO>> get() = _wishlistsPublic

    private val _wishlistsShared: MutableStateFlow<List<BasicWishlistDTO>> = MutableStateFlow(emptyList())
    val wishlistsShared: StateFlow<List<BasicWishlistDTO>> get() = _wishlistsShared


    private val _wishProduct: MutableStateFlow<WishProductDTO?> = MutableStateFlow(null)
    val wishProduct: StateFlow<WishProductDTO?> get() = _wishProduct

    //val username = AccountInfoViewModel

    //Inserimento della wishlist
    fun addWishlist(wishlistName: String, username: String, visibility: Int){
        viewModelScope.launch {
            try{
                doAddWishlist(wishlistName, username, visibility)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun doAddWishlist(wishlistName: String, username: String, visibility: Int) {
        var visibilityType = ""
        when (visibility) {
            0 -> visibilityType = "Pubblica"
            1 -> visibilityType = "Condivisa"
            2 -> visibilityType = "Privata"
        }

        val JSON = "application/json; charset=utf-8".toMediaType()
        val wishlist = WishlistDTO("", wishlistName, visibilityType, username)
        val json = gson.toJson(wishlist)
        val requestBody = json.toRequestBody(JSON)

        val request = Request.Builder()
            .url("http://25.49.50.144:8090/product-api/wishlist")
            .post(requestBody)
            .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
            .build()

        withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                println("response code: ${response.code}")

                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    println("Chiamata fallita o risposta vuota, nell'aggiunta della wishlist. Codice di stato: ${response.code}")
                    return@withContext
                }

                // Update the appropriate StateFlow based on the visibility
                when (visibility) {
                    0 -> _wishlistsPublic.value += BasicWishlistDTO(
                        id = wishlist.id,
                        name = wishlist.name
                    )
                    1 -> _wishlistsShared.value += BasicWishlistDTO(
                        id = wishlist.id,
                        name = wishlist.name
                    )
                    2 -> _wishlistsPrivate.value += BasicWishlistDTO(
                        id = wishlist.id,
                        name = wishlist.name
                    )
                }

                println("wishlist caricata correttamente")
            } catch (e: IOException) {
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
                return@withContext
            }
        }
    }

    //Fine inserimento wishlist


    //Inizio aggiunta prodotto dentro la wishlist
    fun addProductIntoList(wishListId: String, productId: String){
        viewModelScope.launch {
            try{
                doAddProductIntoList(wishListId, productId)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun doAddProductIntoList(wishListId: String, productId: String) {
        println("=== Inizio doAddProductIntoList ===")
        println("Parametro wishListId: $wishListId")
        println("Parametro productId: $productId")

        val JSON = "application/json; charset=utf-8".toMediaType()

        val sendWishlistProductDTO = SendWishlistProductDTO(productId, wishListId)
        val json = gson.toJson(sendWishlistProductDTO)
        println("JSON serializzato: $json")

        val requestBody = json.toRequestBody(JSON)
        val request = Request.Builder()
            .url("http://25.49.50.144:8090/product-api/wishlist/product")
            .post(requestBody)
            .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
            .build()

        println("Request URL: ${request.url}")
        println("Request Headers: ${request.headers}")
        println("Request Body: $json")

        withContext(Dispatchers.IO) {
            try {
                println("=== Inizio chiamata API ===")
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                println("Codice di stato della risposta: ${response.code}")
                println("Messaggio della risposta: ${response.message}")
                println("Corpo della risposta: ${responseBody ?: "Risposta vuota"}")

                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    println("Errore nella risposta dell'aggiunta del prodotto alla wishlist: ${response.message}  ${response.code}")
                    return@withContext
                }

                println("Prodotto aggiunto con successo alla wishlist. Codice di stato: ${response.code}")

            } catch (e: IOException) {
                e.printStackTrace()
                println("Errore durante la chiamata HTTP: ${e.message}")
                return@withContext
            }
        }

        println("=== Fine doAddProductIntoList ===")
    }

    //Fine aggiunta prodotto dentro la wishlist


    //Inizio, Tutti i prodotti dentro la wishlist, per se stesso e per gli utenti
    fun getWishlistProductsByWishlistID(wishlistId: String, ownerUSername: String) {
        viewModelScope.launch {
            try {
                doGetWishlistProductsByWishlistID(wishlistId, ownerUSername)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun doGetWishlistProductsByWishlistID(wishlistId: String, ownerUsername: String) {
        println("=== Inizio doGetWishlistProductsByWishlistID ===")
        println("Parametro wishlistId: $wishlistId")
        println("Parametro ownerUsername: $ownerUsername")

        val manageURL = URL("http://25.49.50.144:8090/product-api/wishlist/products?wish-id=$wishlistId&usr=$ownerUsername")
        println("URL di richiesta: $manageURL")

        val request = Request.Builder()
            .url(manageURL)
            .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
            .build()

        println("Request Headers: ${request.headers}")

        withContext(Dispatchers.IO) {
            try {
                println("=== Inizio chiamata API ===")
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                println("Codice di stato della risposta: ${response.code}")
                println("Messaggio della risposta: ${response.message}")
                println("Corpo della risposta: ${responseBody ?: "Risposta vuota"}")

                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    println("Chiamata fallita o risposta vuota, nella load dei prodotti della wishlist. Codice di stato: ${response.code}")
                    return@withContext
                }

                println("Risposta dal server (corpo): $responseBody")

                val wishListProductType = object : TypeToken<WishProductDTO>() {}.type
                println("Tipo JSON WishProductDTO: $wishListProductType")

                val wishProd = gson.fromJson<WishProductDTO>(responseBody, wishListProductType)
                println("Prodotto desiderato deserializzato: $wishProd")

                _wishProduct.value = wishProd
                println("Prodotti caricati correttamente in _wishProduct: ${_wishProduct.value}")

            } catch (e: IOException) {
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }

        println("=== Fine doGetWishlistProductsByWishlistID ===")
    }

    //Fine, Tutti i prodotti dentro la wishlist, per se stesso e per gli utenti

    //Inizio, presa tutte le wishlist dell'utente con visibilità
    fun getUserWishlists(ownerUsername: String, visibility: Int){
        viewModelScope.launch {
            try{
                doGetUserWishlists(ownerUsername, visibility)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
    //da usare sia per utente stesso che per altri utenti
    suspend fun doGetUserWishlists(ownerUsername: String, visibility: Int) {
        println("=== Inizio doGetUserWishlists ===")
        println("Parametro ownerUsername: $ownerUsername")
        println("Parametro visibility: $visibility")

        val manageURL = URL("http://25.49.50.144:8090/product-api/wishlists?usr=$ownerUsername&visibility=$visibility")
        println("URL generata: $manageURL")

        val token = myToken?.accessToken
        println("Token di accesso: ${token ?: "Nessun token"}")

        val request = Request.Builder()
            .url(manageURL)
            .addHeader("Authorization", "Bearer $token")
            .build()

        println("Request headers: ${request.headers}")

        withContext(Dispatchers.IO) {
            try {
                println("=== Inizio chiamata API ===")
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                println("Codice di stato della risposta: ${response.code}")
                println("Corpo della risposta: ${responseBody ?: "Risposta vuota"}")

                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    println("Chiamata fallita o risposta vuota nella get user wishlist. Codice di stato: ${response.code}")
                    return@withContext
                }

                println("Risposta dal server (corpo della risposta non vuoto): $responseBody")

                val basicWishlist = object : TypeToken<List<BasicWishlistDTO>>() {}.type

                when (visibility) {
                    0 -> {
                        _wishlistsPublic.value = gson.fromJson(responseBody, basicWishlist)
                        println("Aggiornata _wishlistsPublic con: ${_wishlistsPublic.value}")
                    }
                    1 -> {
                        _wishlistsShared.value = gson.fromJson(responseBody, basicWishlist)
                        println("Aggiornata _wishlistsShared con: ${_wishlistsShared.value}")
                    }
                    2 -> {
                        _wishlistsPrivate.value = gson.fromJson(responseBody, basicWishlist)
                        println("Aggiornata _wishlistsPrivate con: ${_wishlistsPrivate.value}")
                    }
                    else -> {
                        println("Valore di visibility non valido: $visibility")
                    }
                }

                println("Lista pubblica attuale: ${_wishlistsPublic.value}")
                println("Lista condivisa attuale: ${_wishlistsShared.value}")
                println("Lista privata attuale: ${_wishlistsPrivate.value}")
                println("Wishlist caricata correttamente")

            } catch (e: Exception) {
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }

        println("=== Fine doGetUserWishlists ===")
    }
    //Fine, presa tutte le wishlist dell'utente con visibilità


    //Inizio, presa tutte le wishlist dell'utente senza visibilità
    fun getAllUserWishlists(){
        viewModelScope.launch {
            try{
                doGetAllUserWishlists()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
    //Prendo id liste
    suspend fun doGetAllUserWishlists(){
        //_wishlists.value = emptyList()
        val manageURL = URL("http://25.49.50.144:8090/product-api/wishlists/all")
        val request = Request.Builder().url(manageURL).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
        withContext(Dispatchers.IO){

            try{
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota, nella get user wishlist. Codice di stato: ${response.code}")
                    return@withContext
                }
                println("Risposta dal server: $responseBody")
                val basicWishlist = object : TypeToken<List<WishlistDTO>>() {}.type
                //_wishlists.value = gson.fromJson(responseBody, basicWishlist)
                println("Wishlist caricata correttamente")

            }catch (e: Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }
    }
    //Fine, presa tutte le wishlist dell'utente senza visibilità

    //Inizio cambio visibilità della wishlist
    fun updateUserWishlists(wishId: String, visibility: Int){
        viewModelScope.launch {
            try{
                doUpdateUserWishlists(wishId, visibility)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun doUpdateUserWishlists(wishId: String, visibility: Int){
        println("WISHIDSSSSSSSSSSS: "+ wishId)
        println("VISSSSSSSS: " + visibility)
        val changeVisibility = ChangeVisibilityDTO(wishId, visibility)
        val manageURL = URL("http://25.49.50.144:8090/product-api/wishlist/visibility");
        val JSON = "application/json; charset=utf-8".toMediaType()
        val json = gson.toJson(changeVisibility)
        val requestBody = json.toRequestBody(JSON)
        val request = Request.Builder().url(manageURL).put(requestBody).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO){

            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Errore nella risposta, nella modifica della visibilità della wishlist: "+response.message+"  "+response.code)
                    return@withContext
                }
                println("response code: ${response.code}")

                //chiedere a luca come gestirlo

            } catch (e: IOException) {
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
                return@withContext
            }
        }
    }
    //Fine cambio visibilità della wishlist

    //Inizio svuota wishlists
    fun deleteWishlist(id: String){
        viewModelScope.launch {
            try{
                doDeleteWishlist(id)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun doDeleteWishlist(id: String){
        val manageURL = URL("http://25.49.50.144:8090/product-api/wishlist/$id")
        val request = Request.Builder().url(manageURL).delete().addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO){
            try{
                val response = client.newCall(request).execute()
                if(!response.isSuccessful){
                    println("Errore nella risposta, nella cancellazione della wishlist: "+response.message+"  "+response.code)
                    return@withContext
                }
                println("response code: ${response.code}")
                //chiedere a luca come gestirlo
            }catch (e: Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
                return@withContext
            }
        }
    }
    //Fine svuota wishlists

    //Inizio elimina il prodotto dentro la wish list
    fun deleteWishlistProductByProductID(wishId: String, productId: String){
        viewModelScope.launch {
            try {
                doDeleteWishlistProductByProductID(wishId, productId)
            }catch (e: Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }
    }

    suspend fun doDeleteWishlistProductByProductID(wishId: String, productId: String){
        val manageURL = URL("http://25.49.50.144:8090/product-api/wishlist/product?wish-id=$wishId&product-id=$productId")
        val request = Request.Builder().url(manageURL).delete().addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
        withContext(Dispatchers.IO) {

            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    println("Errore nella risposta, nella cancellazione del prodotto dalla wishlist: " + response.message + "  " + response.code)
                    return@withContext
                }
                println("response code: ${response.code}")
                for(wishlist in _wishlistsPrivate.value){
                    if(wishlist.id == wishId){
                        _wishlistsPrivate.value = _wishlistsPrivate.value.filter { it.id != wishId }
                        break
                    }
                }
                for(wishlist in _wishlistsPublic.value){
                    if(wishlist.id == wishId){
                        _wishlistsPublic.value = _wishlistsPublic.value.filter { it.id != wishId }
                        break
                    }
                }
                for(wishlist in _wishlistsShared.value){
                    if(wishlist.id == wishId){
                        _wishlistsShared.value = _wishlistsShared.value.filter { it.id != wishId }
                        break
                    }
                }

            }catch (e: Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
                return@withContext
            }
        }
    }
    //Fine elimina il prodotto dentro la wish list

    //Inizio svuota la wishlist
    fun deleteAllWishlistProductsByWishlistID(wishlistId: String){
        viewModelScope.launch {
            try{
                doDeleteAllWishlistProductsByWishlistID(wishlistId)
            }catch (e: Exception){
                e.printStackTrace()

            }
        }
    }

    suspend fun doDeleteAllWishlistProductsByWishlistID(wishlistId: String){
        val manageURL = URL("http://25.49.50.144:8090/product-api/wishlist/products?wish-id=$wishlistId")
        val request = Request.Builder().url(manageURL).delete().addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO){

            try{
                val response = client.newCall(request).execute()
                if(!response.isSuccessful){
                    println("Errore nella risposta, nella cancellazione della wishlist: "+response.message+"  "+response.code)
                    return@withContext
                }
                println("response code: ${response.code}")
                //chiedere a luca come gestirlo
            }
            catch (e: Exception){
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }
    }
    //Fine svuota la wishlist



}
class WishlistViewModelFactory(
    private val wishlistRepository: WishlistRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WishlistViewModel::class.java)) {
            return WishlistViewModel(wishlistRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}