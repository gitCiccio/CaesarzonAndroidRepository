package com.example.caesarzonapplication.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.caesarzonapplication.ui.screens.HomeScreen

@Composable
fun NavigationBottomBar(navController: NavHostController) {

    var navigationSelectedItem by remember {
        mutableStateOf<BottomBarScreen>(BottomBarScreen.Home)
    }

    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Profile,
        BottomBarScreen.Cart,
        BottomBarScreen.Friends,
        BottomBarScreen.Wishlist
    )

    NavigationBar(
        containerColor = Color(100, 104, 208)
    ) {
        screens.forEach { screen ->
            NavigationBarItem(
                selected = screen == navigationSelectedItem,
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title,
                    )
                },
                onClick = {
                    navigationSelectedItem = screen
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color(238, 137, 60, 129),
                    selectedIconColor = Color(238, 137, 60, 255),
                    unselectedIconColor = Color.Black,
                    selectedTextColor = Color(238, 137, 60, 255),
                    unselectedTextColor = Color.Black
                )
            )
        }
    }
}

@Composable
@Preview
fun NavigationBottomBarPreview() {
    NavigationBottomBar(rememberNavController())
}

