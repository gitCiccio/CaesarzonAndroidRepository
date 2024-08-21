package com.example.caesarzonapplication.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AdminBottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
){
    data object Home: AdminBottomBarScreen(
        route = "adminHome",
        title = "HOME",
        icon = Icons.Default.Home
    )

    data object AddProduct: AdminBottomBarScreen(
        route = "addProduct",
        title = "ADD PRODUCT",
        icon = Icons.Default.AddCircle
    )

    data object SearchUser: AdminBottomBarScreen(
        route = "searchUser",
        title = "SEARCH USER",
        icon = Icons.Default.PersonSearch
    )

    data object Reports: AdminBottomBarScreen(
        route = "reports",
        title = "REPORTS",
        icon = Icons.Default.Report
    )

    data object SupportRequest: AdminBottomBarScreen(
        route = "supportRequest",
        title = "SUPPORT REQUEST",
        icon = Icons.Default.SupportAgent
    )
}