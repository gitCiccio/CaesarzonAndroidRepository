package com.example.caesarzonapplication.model.viewmodels
import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.dto.ProductDTO
import com.example.caesarzonapplication.model.dto.ProductSearchDTO
import com.example.caesarzonapplication.model.dto.ProductSearchWithImage
import com.example.caesarzonapplication.model.dto.ProductWithImage
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.basicToken
import com.example.caesarzonapplication.model.utils.BitmapConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
    val bitampConverter = BitmapConverter()

    //private val _productsInShoppingCart = mutableStateListOf<Product>()
    //val productInShoppingCart: List<Product> get() = _productsInShoppingCart

    var selectedProduct = mutableStateOf<ProductWithImage?>(null)

    private val _newProducts = mutableStateListOf<ProductSearchWithImage>()
    val newProducts: List<ProductSearchWithImage> get() = _newProducts


    private val _hotProducts = mutableStateListOf<ProductSearchWithImage>()
    val hotProducts: List<ProductSearchWithImage> get() = _hotProducts



    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val _productList = MutableStateFlow<List<ProductSearchWithImage>>(emptyList())
    val productList: StateFlow<List<ProductSearchWithImage>> = _productList

    /*fun addProductToCart(product: Product) {
        _productsInShoppingCart.add(product)
    }*/

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
                    val image = loadProductImage(productDTO.id)
                    selectedProduct.value = ProductWithImage(productDTO, image)

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
                val newProducts: List<ProductSearchDTO> = gson.fromJson(responseBody, productListType)

                for (product in newProducts) {
                    println("prodotto: " + product.productId)
                }
                _newProducts.clear()

                for(product in newProducts){
                    val image = loadProductImage(product.productId.toString())
                    _newProducts.add(ProductSearchWithImage(product, image))
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    // Updated loadProductImage to handle base64 encoded strings
    suspend fun loadProductImage(productId: String): Bitmap? {
        println("id che mando: $productId")
        return withContext(Dispatchers.IO) {
            val uuidProduct = UUID.fromString(productId)
            val manageURL = URL("http://25.49.50.144:8090/product-api/image/$uuidProduct")
            val request = Request.Builder()
                .url(manageURL)
                .addHeader("Authorization", "Bearer ${basicToken?.accessToken}")
                .build()

            try {
                val response = client.newCall(request).execute()
                if (!response.isSuccessful || response.body == null) {
                    println("Problemi nel caricamento del prodotto" + response.message + " code: " + response.code)
                    return@withContext null
                }
                val imageByteArray = response.body?.byteStream()?.readBytes()

                // Check if imageByteArray is not null
                if (imageByteArray != null) {
                    val convertedImage = bitampConverter.converterByteArray2Bitmap(imageByteArray)
                    println("prodotto preso response: " + response.message + " " + response.code)
                    return@withContext convertedImage
                } else {
                    println("Response body is null")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                println("Errore nel caricamento dell'immagine")
            }
            return@withContext null
        }
    }

    suspend fun loadHotProducts() {
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
                val newProducts: List<ProductSearchDTO> = gson.fromJson(responseBody, productListType)

                _hotProducts.clear()
               for (product in newProducts) {
                   val image =loadProductImage(product.productId.toString())
                   _hotProducts.add(ProductSearchWithImage(product, image))
               }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun searchProducts(query: String, minPrice: Double, maxPrice: Double, isClothing: Boolean) {
        _productList.value = emptyList()
        viewModelScope.launch {
            val manageURL = URL("http://25.49.50.144:8090/product-api/search?search-text=$query&min-price=$minPrice&max-price=$maxPrice&is-clothing=$isClothing")
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
                    val productListType = object : TypeToken<List<ProductSearchDTO>>() {}.type
                    val newProducts: List<ProductSearchDTO> = gson.fromJson(responseBody, productListType)

                    // Asynchronously load images for each product
                    _productList.value = emptyList()

                    for(product in newProducts){
                        val image = loadProductImage(product.productId.toString())
                        _productList.value += ProductSearchWithImage(product, image)
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    _isLoading.value = false
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
                        println("Failed to fetch products by category. Response code: ${response.code}")
                        return@withContext
                    }

                    val responseBody = response.body?.string()
                    val productListType = object : TypeToken<List<ProductSearchDTO>>() {}.type
                    val newProducts: List<ProductSearchDTO> = gson.fromJson(responseBody, productListType)

                    // Asynchronously load images for each product
                    _productList.value = emptyList()

                    for(product in newProducts){
                        val image = loadProductImage(product.productId.toString())
                        _productList.value += ProductSearchWithImage(product, image)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    println("Error occurred while fetching products by category.")
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

}
