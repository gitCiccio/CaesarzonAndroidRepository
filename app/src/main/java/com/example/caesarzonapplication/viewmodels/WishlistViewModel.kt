package com.example.caesarzonapplication.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.caesarzonapplication.model.dto.SingleWishlistProductDTO
import com.example.caesarzonapplication.model.dto.WishlistDTO
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.io.IOException
import java.net.URL
import java.util.UUID

class WishlistViewModel: ViewModel(){

    private val client = OkHttpClient()
    private val _wishlists = mutableStateListOf<WishlistDTO>()
    val wishlists: List<WishlistDTO> get() = _wishlists
    private val products = mutableStateListOf<SingleWishlistProductDTO>()
    val productsList: List<SingleWishlistProductDTO> get() = products
    val username = AccountInfoViewModel.UserData.accountInfoData.value.username


    fun loadWishlists(visibility: Int){
        println("Sto caricando le liste desideri di : $username")
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
                    println("Wishlist name: $name")
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return@launch
            }
        }
    }

    fun getWishlistProducts(wishlistId: UUID): List<SingleWishlistProductDTO>? {
        products.clear()
        for (wishlist in _wishlists) {
            if (wishlist.id == wishlistId) {
                val manageURL = URL("http://25.25.161.198:8090/product-api/wishlistProducts?wish_id=$wishlistId");
                val request = Request.Builder().url(manageURL).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
                try {
                    val response = client.newCall(request).execute()
                    val responseBody = response.body?.string()
                    val jsonResponse = JSONArray(responseBody)
                    for (i in 0 until jsonResponse.length()) {
                        val productName = jsonResponse.getJSONObject(i).getString("productName")
                        val price = jsonResponse.getJSONObject(i).optString("price", "")
                        products.add(SingleWishlistProductDTO(productName,price.toDouble() ))
                        println("Product name: $productName")
                    }
                    return products
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return null
    }
}