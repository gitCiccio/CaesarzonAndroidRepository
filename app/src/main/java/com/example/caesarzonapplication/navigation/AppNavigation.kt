package com.example.caesarzonapplication.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.caesarzonapplication.ui.components.MenuFloatingButton
import com.example.caesarzonapplication.ui.components.NavigationBottomBar
import com.example.caesarzonapplication.ui.screens.AccountScreen
import com.example.caesarzonapplication.ui.screens.HomeScreen
import com.example.caesarzonapplication.ui.screens.SettingsScreen
import com.example.caesarzonapplication.ui.screens.ShoppingCartScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    Scaffold (
        topBar = {},
        bottomBar = { NavigationBottomBar(navController)  },
        content = { padding ->
            NavHost(navController = navController, startDestination = "Home"){
                composable("Home"){HomeScreen()}
                composable("ShoppingCart"){ ShoppingCartScreen()}
                composable("Account"){AccountScreen()}
                composable("Settings"){SettingsScreen()}
            }
        },
        floatingActionButton = {MenuFloatingButton()},
        floatingActionButtonPosition = FabPosition.End
    )

}

