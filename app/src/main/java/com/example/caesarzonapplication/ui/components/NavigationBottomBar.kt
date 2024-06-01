package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.caesarzonapplication.R

@Composable
fun NavigationBottomBar(navController: NavHostController){
    //selectedIndex: MutableState<Int>, dovreppe essere un parametro delle fun
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    //Creazione della bottom bar
    NavigationBar(
        containerColor = Color(100,104,208)
    ) {//è un componente che rappresenta la barra inferiore

        NavigationBarItem(
            selected = currentDestination?.route == "home",
            onClick = { navController.navigate("home") },
            icon = {
                Icon(
                    Icons.Filled.Home,
                    contentDescription = stringResource(R.string.home),
                    tint = if (currentDestination?.route == "home") Color(238, 137, 60, 255) else Color.Black)
            }
        )

        NavigationBarItem(
            selected = currentDestination?.route == "userInfo",
            onClick = { navController.navigate("userInfo") },
            icon = { Icon(Icons.Filled.AccountCircle, contentDescription = stringResource(R.string.userInfo),
                tint = if (currentDestination?.route == "userInfo") Color(238, 137, 60, 255) else Color.Black)
            }
        )

        NavigationBarItem(
            selected = currentDestination?.route == "shopcart",
            onClick = { navController.navigate("shopcart") },
            icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = stringResource(R.string.shopcart),
                tint = if (currentDestination?.route == "shopcart") Color(238, 137, 60, 255) else Color.Black)
            }
        )

        NavigationBarItem(
            selected = currentDestination?.route == "settings",
            onClick = { navController.navigate("settings")},
            icon = { Icon(Icons.Filled.Menu, contentDescription = stringResource(R.string.settings),
                tint = if (currentDestination?.route == "settings") Color(238, 137, 60, 255) else Color.Black)
            }
        )
    }
}

