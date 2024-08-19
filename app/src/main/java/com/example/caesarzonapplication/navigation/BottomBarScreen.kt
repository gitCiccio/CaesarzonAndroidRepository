package com.example.caesarzonapplication.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
){
    data object Home: BottomBarScreen(
        route = "home",
        title = "HOME",
        icon = Icons.Default.Home
    )

    data object Profile: BottomBarScreen(
        route = "profile",
        title = "PROFILE",
        icon = Icons.Default.Person
    )

    data object Cart: BottomBarScreen(
        route = "shopcart",
        title = "CART",
        icon = Icons.Default.ShoppingCart
    )

    data object Friends: BottomBarScreen(
        route = "friends",
        title = "FRIENDS",
        icon = Icons.Default.Favorite
    )

    data object Wishlist: BottomBarScreen(
        route = "wishlist",
        title = "WISHLIST",
        icon = Icons.Default.Checklist
    )
}