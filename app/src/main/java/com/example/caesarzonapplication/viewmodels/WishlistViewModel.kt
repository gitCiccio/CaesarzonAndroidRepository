package com.example.caesarzonapplication.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.caesarzonapplication.model.dto.SingleWishlistProductDTO
import com.example.caesarzonapplication.model.dto.WishlistDTO
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URL
import java.util.UUID

class WishlistViewModel: ViewModel(){

    private val client = OkHttpClient()
    private val _wishlists = mutableStateListOf<WishlistDTO>()
    val wishlists: List<WishlistDTO> get() = _wishlists
    private val _products = mutableStateListOf<SingleWishlistProductDTO>()
    val products: List<SingleWishlistProductDTO> get() = _products
    val username = AccountInfoViewModel.UserData.accountInfoData.value.username

    fun addWishlist(wishlistName: String, visibility: Int){
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
        val client = OkHttpClient()
        val json = JSONObject().apply {
            put("name", wishlistName)
            put("visibility", visibilityType)
        }
        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = json.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url("http://25.49.50.144:8090/product-api/wishlist")
            .post(requestBody)
            .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = client.newCall(request).execute()
                if(!response.isSuccessful){
                    println("Errore nella risposta: "+response.message+"  "+response.code)
                    return@launch
                }
                loadWishlists(visibility)
            } catch (e: IOException) {
                e.printStackTrace()
                return@launch
            }
        }
    }

    fun changeWishlistVisibility(wishlistId: UUID, visibility: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val manageURL = URL("http://25.49.50.144:8090/product-api/wishlist/$wishlistId?visibility=$visibility");
            val request = Request.Builder().url(manageURL).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
            try {
                val response = client.newCall(request).execute()
                if(!response.isSuccessful){
                    println("Errore nella risposta: "+response.message+"  "+response.code)
                    return@launch
                }
                else{
                    println("Sto modificando la visibilità della wishlist: $wishlistId "+response.message+"  "+response.code)
                    loadWishlists(visibility)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return@launch
            }
        }
    }

    fun addProductToWishlist(wishlistName: String, visibility: Int){
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
        val client = OkHttpClient()
        val formBody = FormBody.Builder()
            .add("name", wishlistName)
            .add("visibility", visibilityType)
            .build()
        val request = Request.Builder()
            .url("http://25.49.50.144:8090/product-api/wishlist/product")
            .post(formBody)
            .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = client.newCall(request).execute()
                if(!response.isSuccessful){
                    println("Errore nella risposta: "+response.message+"  "+response.code)
                    return@launch
                }
                loadWishlists(visibility)
            } catch (e: IOException) {
                e.printStackTrace()
                return@launch
            }
        }
    }

    fun loadWishlists(visibility: Int){
        CoroutineScope(Dispatchers.IO).launch {
            _wishlists.clear()
            val manageURL = URL("http://25.49.50.144:8090/product-api/wishlists?usr=$username&visibility=$visibility");
            val request = Request.Builder().url(manageURL).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
            try {
                val response = client.newCall(request).execute()
                if(!response.isSuccessful){
                    println("Errore nella risposta: "+response.message+"  "+response.code)
                    return@launch
                }
                val responseBody = response.body?.string()
                val jsonResponse = JSONArray(responseBody)
                for (i in 0 until jsonResponse.length()) {
                    val id = jsonResponse.getJSONObject(i).getString("id")
                    val name = jsonResponse.getJSONObject(i).optString("name", "")
                    _wishlists.add(WishlistDTO(UUID.fromString(id), name))
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return@launch
            }
        }
    }

    suspend fun getWishlistProducts(wishlistId: UUID): List<SingleWishlistProductDTO>? {
        _products.clear()
        return withContext(Dispatchers.IO) {
            val productsTemp = mutableListOf<SingleWishlistProductDTO>()
            val wishlist = _wishlists.find { it.id == wishlistId }
            if (wishlist != null) {
                val manageURL = URL("http://25.25.161.198:8090/product-api/wishlist/products?wish-id=$wishlistId")
                val request = Request.Builder()
                    .url(manageURL)
                    .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
                    .build()
                try {
                    val response = client.newCall(request).execute()
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        val jsonResponse = try {
                            val jsonObject = JSONObject(responseBody)
                            jsonObject.getJSONArray("singleWishListProductDTOS")
                        } catch (e: JSONException) {
                            return@withContext null
                        }
                        for (i in 0 until jsonResponse.length()) {
                            val jsonObject = jsonResponse.getJSONObject(i)
                            val id = jsonObject.getString("productId")
                            val productName = jsonObject.getString("productName")
                            val price = jsonObject.optDouble("price", 0.0)
                            productsTemp.add(SingleWishlistProductDTO(UUID.fromString(id), productName, price))
                        }
                        withContext(Dispatchers.Main) {
                            _products.addAll(productsTemp)
                        }
                        return@withContext products
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return@withContext null
        }
    }

    fun deleteWishlist(wishlistId: UUID) {
        CoroutineScope(Dispatchers.IO).launch {
            val manageURL = URL("http://25.49.50.144:8090/product-api/wishlist/$wishlistId")
            val request = Request.Builder()
                .url(manageURL)
                .delete()
                .addHeader("Authorization", "Bearer ${myToken?.accessToken}")
                .build()
            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    println("Errore nella risposta: " + response.message + "  " + response.code)
                    return@launch
                }
                _wishlists.removeIf { it.id == wishlistId }
                println("Sto eliminando la wishlist: $wishlistId")
                if (_products.any { it.productId == wishlistId }) {
                    _products.clear()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return@launch
            }
        }
    }

    fun emptyWishlist(wishlistId: UUID) {
        CoroutineScope(Dispatchers.IO).launch {
            val manageURL = URL("http://25.49.50.144:8090/product-api/wishlist/products?wish-id="+wishlistId);
            val request = Request.Builder().url(manageURL).delete().addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
            try {
                val response = client.newCall(request).execute()
                for (wishlist in _wishlists){
                    if(wishlist.id == wishlistId){
                        println("Sto svuotando la wishlist: $wishlistId")
                        _products.clear()
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return@launch
            }
        }
    }

    fun removeWishlistProduct(wishlistId: UUID, productId: UUID) {
        println("$wishlistId $productId")
        CoroutineScope(Dispatchers.IO).launch {
            val manageURL = URL("http://25.49.50.144:8090/product-api/wishlist/product?wish-id="+wishlistId+"&product-id="+productId);
            val request = Request.Builder().url(manageURL).delete().addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
            try {
                val response = client.newCall(request).execute()
                if(!response.isSuccessful){
                    println("Errore nella risposta: "+response.message+"  "+response.code)
                    return@launch
                }
                // Colleziona gli elementi da rimuovere
                val productsToRemove = _products.filter { it.productId == productId }
                // Rimuovi gli elementi al di fuori del ciclo
                _products.removeAll(productsToRemove)
                println("Sto eliminando il prodotto dalla wishlist: $wishlistId")
            } catch (e: IOException) {
                e.printStackTrace()
                return@launch
            }
        }
    }
}