package com.example.caesarzonapplication.model.viewmodels
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.dto.ProductDTO
import com.example.caesarzonapplication.model.dto.ProductSearchDTO
import com.example.caesarzonapplication.model.entities.shoppingCartEntities.Product
import com.example.caesarzonapplication.model.service.KeycloakService
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.basicToken
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.URL
import java.util.UUID

class ProductsViewModel: ViewModel() {

    val client = OkHttpClient()
    val gson = Gson()

    private val _productsInShoppingCart = mutableStateListOf<Product>()
    val productInShoppingCart: List<Product> get() = _productsInShoppingCart

    val selectedProduct = mutableStateOf<ProductDTO?>(null)

    private val _newProducts = mutableStateListOf<ProductSearchDTO>()
    val newProducts: List<ProductSearchDTO> get() = _newProducts

    private val _hotProducts = mutableStateListOf<ProductSearchDTO>()
    val hotProducts: List<ProductSearchDTO> get() = _hotProducts

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val _productList = MutableStateFlow<List<ProductSearchDTO>>(emptyList())
    val productList: StateFlow<List<ProductSearchDTO>> = _productList

    fun addProductToCart(product: Product) {
        _productsInShoppingCart.add(product)
    }

    fun getProduct(productID: UUID){
        viewModelScope.launch(Dispatchers.IO)
        {
            val manageURL = URL("http://25.49.50.144:8090/product-api/product/$productID")
            val request = Request
                .Builder()
                .url(manageURL)
                .addHeader("Authorization", "Bearer ${basicToken?.accessToken}")
                .build()
            try{
                println("Sono nella funzione per prendere il prodotto")
                val response = client.newCall(request).execute()
                println("sto prendendo il prodotto"+response.message+" code: "+response.code)
                if (!response.isSuccessful) {
                    println("Problemi nel caricamento del prodotto"+response.message+" code: "+response.code)
                    return@launch
                }else{
                    val responseBody = response.body?.string()

                    val productDTO = gson.fromJson(responseBody, ProductDTO::class.java)
                    println("prodotto preso response: "+response.message+" "+response.code)
                    selectedProduct.value = productDTO
                    println("Prodotto : "+productDTO.name)
                }

            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    suspend fun loadNewProducts() {
        withContext(Dispatchers.IO) {
            val manageURL = URL("http://25.49.50.144:8090/product-api/new")
            val request = Request.Builder()
                .url(manageURL)
                .addHeader("Authorization", "Bearer ${basicToken?.accessToken}")
                .build()
            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    return@withContext
                }
                val responseBody = response.body?.string()
                val productListType = object : TypeToken<List<ProductSearchDTO>>() {}.type
                _newProducts.clear()
                _newProducts.addAll(gson.fromJson(responseBody, productListType))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    suspend fun loadHotProducts(){
        withContext(Dispatchers.IO) {
            val manageURL = URL("http://25.49.50.144:8090/product-api/product/offer")
            val request = Request.Builder()
                .url(manageURL)
                .addHeader("Authorization", "Bearer ${basicToken?.accessToken}")
                .build()
            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful) {
                    return@withContext
                }
                val responseBody = response.body?.string()
                val productListType = object : TypeToken<List<ProductSearchDTO>>() {}.type
                _hotProducts.clear()
                _hotProducts.addAll(gson.fromJson(responseBody, productListType))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun searchProducts(query: String) {
        _productList.value = emptyList()

        viewModelScope.launch {
            val manageURL = URL("http://25.49.50.144:8090/product-api/search?search-text=$query")
            val request = Request.Builder()
                .url(manageURL)
                .addHeader("Authorization", "Bearer ${basicToken?.accessToken}")
                .build()
            _isLoading.value = true
            withContext(Dispatchers.IO) {
                try {
                    val response = client.newCall(request).execute()
                    if (!response.isSuccessful) {
                        return@withContext
                    }
                    val responseBody = response.body?.string()
                    val productList: List<ProductSearchDTO> = gson.fromJson(
                        responseBody,
                        object : TypeToken<List<ProductSearchDTO>>() {}.type
                    )
                    _productList.value = productList
                    _isLoading.value = false
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

        }

    }

    fun getProductsByCategory(category: String) {
        _productList.value = emptyList()

        viewModelScope.launch {
            val manageURL = URL("http://25.49.50.144:8090/product-api/search?search-text=$category")
            val request = Request.Builder()
                .url(manageURL)
                .addHeader("Authorization", "Bearer ${basicToken?.accessToken}")
                .build()
            _isLoading.value = true
            withContext(Dispatchers.IO) {
                try {
                    val response = client.newCall(request).execute()
                    if (!response.isSuccessful) {
                        return@withContext
                    }
                    val responseBody = response.body?.string()
                    val productList: List<ProductSearchDTO> = gson.fromJson(responseBody, object : TypeToken<List<ProductSearchDTO>>() {}.type)
                    for (product in productList) {
                        println(product.productName)
                    }
                    _productList.value = productList
                    _isLoading.value = false
                }
                catch (e: IOException) {
                    e.printStackTrace()
                }
            }

        }
    }

}
