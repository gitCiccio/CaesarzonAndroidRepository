package com.example.caesarzonapplication.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.caesarzonapplication.model.dto.BanDTO
import com.example.caesarzonapplication.model.dto.UserFindDTO
import com.example.caesarzonapplication.model.dto.UserSearchDTO
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import java.io.IOException
import java.net.URL
import java.time.LocalDate

class BanViewModel : ViewModel() {


    val client = OkHttpClient()

    //Rendere i numeri per le chiamate dinamici

    //da capire come fare l'init
    init {

    }


}