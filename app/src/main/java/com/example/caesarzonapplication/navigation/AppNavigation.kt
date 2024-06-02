package com.example.caesarzonapplication.navigation

import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.caesarzonapplication.ui.components.MenuFloatingButton
import com.example.caesarzonapplication.ui.components.NavigationBottomBar
import com.example.caesarzonapplication.ui.screens.AccountScreen
import com.example.caesarzonapplication.ui.screens.HomeScreen
import com.example.caesarzonapplication.ui.screens.SettingsScreen
import com.example.caesarzonapplication.ui.screens.ShoppingCartScreen
import com.example.caesarzonapplication.viewmodels.HomeViewModel

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
                composable("shopcart"){ ShoppingCartScreen(padding, viewModel())}
                composable("userInfo"){AccountScreen(padding)}
                composable("settings"){SettingsScreen(padding)}
            }
        },
        floatingActionButton = {MenuFloatingButton()},
        floatingActionButtonPosition = FabPosition.End
    )

}



