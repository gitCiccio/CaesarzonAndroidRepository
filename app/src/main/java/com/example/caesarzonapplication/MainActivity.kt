package com.example.caesarzonapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.compose.rememberNavController
import com.example.caesarzonapplication.model.viewmodels.AccountInfoViewModel
import com.example.caesarzonapplication.model.viewmodels.NotificationViewModel
import com.example.caesarzonapplication.model.viewmodels.ProductsViewModel
import com.example.caesarzonapplication.ui.screens.MainScreen
import com.example.caesarzonapplication.ui.theme.CaesarzonApplicationTheme

class MainActivity : ComponentActivity() {

    private val productsViewModel: ProductsViewModel by viewModels()
    private val accountInfoViewModel: AccountInfoViewModel by viewModels()
    private val notificationViewModel: NotificationViewModel by viewModels()
    private val wishlistViewModel: ProductsViewModel by viewModels()
    private val friendsViewModel: ProductsViewModel by viewModels()
    private var isAdmin = mutableStateOf(false)
    private var logged = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            CaesarzonApplicationTheme{
                val navController = rememberNavController()
                MainScreen(
                    navController = navController,
                    isAdmin,
                    logged,
                    accountInfoViewModel = accountInfoViewModel,
                    productsViewModel = productsViewModel,
                    notificationViewModel = notificationViewModel
                )
            }
        }
    }
}


