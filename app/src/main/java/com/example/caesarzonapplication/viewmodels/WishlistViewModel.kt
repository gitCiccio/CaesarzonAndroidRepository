package com.example.caesarzonapplication.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.caesarzonapplication.model.UserFindDTO
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

    private val accountInfoViewModel = AccountInfoViewModel()
    private val client = OkHttpClient()
    private val _wishlists = mutableStateListOf<WishlistDTO>()
    val wishlists: List<WishlistDTO> get() = _wishlists
    val username = accountInfoViewModel.accountInfoData.value.username


    fun loadWishlists(visibility: Int){
        CoroutineScope(Dispatchers.IO).launch {
            _wishlists.clear()
            val manageURL = URL("http://25.49.50.144:8090/product-api/wishlists?usr=$username&visibility=$visibility");
            val request = Request.Builder().url(manageURL).addHeader("Authorization", "Bearer ${myToken?.accessToken}").build()
            try {
                val response = client.newCall(request).execute()
                if(!response.isSuccessful){
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
}