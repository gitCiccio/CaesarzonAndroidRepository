package com.example.caesarzonapplication.model.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.dto.ChangeVisibilityDTO
import com.example.caesarzonapplication.model.dto.SendWishlistProductDTO
import com.example.caesarzonapplication.model.dto.SingleWishlistProductDTO
import com.example.caesarzonapplication.model.dto.WishProductDTO
import com.example.caesarzonapplication.model.dto.WishlistDTO
import com.example.caesarzonapplication.model.repository.userRepository.ProfileImageRepository
import com.example.caesarzonapplication.model.repository.userRepository.UserRepository
import com.example.caesarzonapplication.model.repository.wishlistRepository.WishlistRepository
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AccountInfoViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URL
import java.util.UUID

class WishlistViewModel(private val wishlistRepository: WishlistRepository): ViewModel(){

    private val client = OkHttpClient()
    private val gson = Gson()

    private val _wishlists: MutableStateFlow<List<WishlistDTO>> = MutableStateFlow(emptyList())
    val wishlists: StateFlow<List<WishlistDTO>> get() = _wishlists

    private val _products = mutableStateListOf<SingleWishlistProductDTO>()
    val products: List<SingleWishlistProductDTO> get() = _products
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

    suspend fun doAddWishlist(wishlistName: String, username: String,visibility: Int){
        var visibilityType = ""
        when (visibility){
            0 ->{
                visibilityType="Pubblica"
            }
            1 ->{
                visibilityType="Condivisa"
            }
            2 ->{
                visibilityType="Privata"
            }
        }
        val JSON = "application/json; charset=utf-8".toMediaType()
        val json = gson.toJson(wishlists)
        val requestBody = json.toRequestBody(JSON)

        val request = Request.Builder().url("http://25.49.50.144:8090/product-api/wishlist").post(requestBody).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()

        withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                println("response code: ${response.code}")
                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Chiamata fallita o risposta vuota, nell'aggiunta della wishlist. Codice di stato: ${response.code}")
                    return@withContext
                }
                _wishlists.value.toMutableList().add(WishlistDTO("", wishlistName, visibilityType, username))
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
    fun addProductIntoList(singleWishlistProductDTO: SendWishlistProductDTO){
        viewModelScope.launch {
            try{
                doAddProductIntoList(singleWishlistProductDTO)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun doAddProductIntoList(singleWishlistProductDTO: SendWishlistProductDTO){
        val JSON = "application/json; charset=utf-8".toMediaType()
        val json = gson.toJson(singleWishlistProductDTO)
        val requestBody = json.toRequestBody(JSON)
        val request = Request.Builder()
            .url("http://25.49.50.144:8090/product-api/wishlist/product")
            .post(requestBody)
            .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
            .build()

        withContext(Dispatchers.IO){
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                if(!response.isSuccessful || responseBody.isNullOrEmpty()){
                    println("Errore nella risposta dell'aggiunta del prodotto alla wishlist: "+response.message+"  "+response.code)
                    return@withContext
                }
                println("response code: ${response.code}")

                //chiedere a luca come gestirlo

            } catch (e: IOException) {
                e.printStackTrace()
                return@withContext
            }
        }
    }
    //Fine aggiunta prodotto dentro la wishlist


    //Inizio, Tutti i prodotti dentro la wishlist, per se stesso e per gli utenti
    fun getWishlistProductsByWishlistID(wishlistId: UUID, ownerUSername: String) {
        viewModelScope.launch {
            try {
                doGetWishlistProductsByWishlistID(wishlistId, ownerUSername)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun doGetWishlistProductsByWishlistID(wishlistId: UUID, ownerUSername: String) {
        _products.clear()

        val manageURL = URL("http://25.25.161.198:8090/product-api/wishlist/products?wish-id=$wishlistId&usr=$ownerUSername")
        val request = Request.Builder()
            .url(manageURL)
            .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
            .build()
        withContext(Dispatchers.IO) {

            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    println("Chiamata fallita o risposta vuota, nella load dei prodotti della wishlist. Codice di stato: ${response.code}")
                    return@withContext
                }
                println("Risposta dal server: $responseBody")

                val wishListProduct = object : TypeToken<WishProductDTO>() {}.type
                val wishListProductDTO = gson.fromJson<WishProductDTO>(responseBody, wishListProduct)
                //_products.addAll(wishListProductDTO.products)
                println("Prodotti caricati correttamente")

            } catch (e: IOException) {
                e.printStackTrace()
                println("Errore durante la chiamata: ${e.message}")
            }
        }
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

    suspend fun doGetUserWishlists(ownerUsername: String, visibility: Int){
        _wishlists.value = emptyList()
        val manageURL = URL("http://25.49.50.144:8090/product-api/wishlists?usr=$ownerUsername&visibility=$visibility")
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

    suspend fun doGetAllUserWishlists(){
        _wishlists.value = emptyList()
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
    fun getUserWishlists(changeVisibilityDTO: ChangeVisibilityDTO){
        viewModelScope.launch {
            try{
                doGetUserWishlists(changeVisibilityDTO)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun doGetUserWishlists(changeVisibilityDTO: ChangeVisibilityDTO){
        val manageURL = URL("http://25.49.50.144:8090/product-api/wishlist/visibility");
        val JSON = "application/json; charset=utf-8".toMediaType()
        val json = gson.toJson(changeVisibilityDTO)
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
                //chiedere a luca come gestirlo

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