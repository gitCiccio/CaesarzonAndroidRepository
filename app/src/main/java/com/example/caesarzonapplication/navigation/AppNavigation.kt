package com.example.caesarzonapplication.navigation

import ShoppingCartScreen
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.caesarzonapplication.ui.components.MenuFloatingButton
import com.example.caesarzonapplication.ui.components.NavigationBottomBar
import com.example.caesarzonapplication.ui.screens.AccountScreen
import com.example.caesarzonapplication.ui.screens.FriendlistScreen
import com.example.caesarzonapplication.ui.screens.HomeScreen
import com.example.caesarzonapplication.viewmodels.HomeViewModel
import com.example.caesarzonapplication.viewmodels.ProductsViewModel
import com.example.caesarzonapplication.viewmodels.UserViewModel

@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    Scaffold (
        topBar = {},
        bottomBar = { NavigationBottomBar(navController)  },
        content = { padding ->
            NavHost(
                navController = navController,
                startDestination = "home"
            ){
                composable("home"){HomeScreen(padding, homeViewModel = HomeViewModel())}
                composable("shopcart"){ ShoppingCartScreen(padding, shoppingCartViewModel = ProductsViewModel(), homeViewModel = HomeViewModel())}
                composable("userInfo"){AccountScreen(padding)}
                composable("friendlist"){ FriendlistScreen(userViewModel = UserViewModel()) }
            }
        },
        floatingActionButton = {MenuFloatingButton()},
        floatingActionButtonPosition = FabPosition.End
    )

}



