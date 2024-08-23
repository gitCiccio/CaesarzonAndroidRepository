package com.example.caesarzonapplication.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AccountTabRow(
    val name: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
){
    data object Profile: AccountTabRow(
        name = "Profilo",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )

    data object Addresses: AccountTabRow(
        name = "Indirizzi",
        selectedIcon = Icons.Filled.Map,
        unselectedIcon = Icons.Outlined.Map
    )

    data object Cards: AccountTabRow(
        name = "Carte",
        selectedIcon = Icons.Filled.CreditCard,
        unselectedIcon = Icons.Outlined.CreditCard
    )

    data object Orders: AccountTabRow(
        name = "Ordini",
        selectedIcon = Icons.Filled.ShoppingBag,
        unselectedIcon = Icons.Outlined.ShoppingBag
    )

    data object Returns: AccountTabRow(
        name = "Resi",
        selectedIcon = Icons.Filled.Replay,
        unselectedIcon = Icons.Outlined.Replay
    )

    data object Support: AccountTabRow(
        name = "Assistenza",
        selectedIcon = Icons.Filled.SupportAgent,
        unselectedIcon = Icons.Outlined.SupportAgent
    )
}