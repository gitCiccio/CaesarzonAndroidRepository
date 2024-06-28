package com.example.caesarzonapplication.navigation

import ShoppingCartScreen
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.caesarzonapplication.ui.components.LoginPopup
import com.example.caesarzonapplication.ui.components.MenuFloatingButton
import com.example.caesarzonapplication.ui.components.NavigationBottomBar
import com.example.caesarzonapplication.ui.screens.AccountScreen
import com.example.caesarzonapplication.ui.screens.AdminScreen
import com.example.caesarzonapplication.ui.screens.FriendlistScreen
//import com.example.caesarzonapplication.ui.screens.FriendlistScreen
import com.example.caesarzonapplication.ui.screens.HomeScreen
import com.example.caesarzonapplication.ui.screens.ProductDetailsScreen
import com.example.caesarzonapplication.ui.screens.UserPageScreen
import com.example.caesarzonapplication.ui.screens.WishlistScreen
import com.example.caesarzonapplication.viewmodels.AccountInfoViewModel
import com.example.caesarzonapplication.viewmodels.AdminInfoViewModel
import com.example.caesarzonapplication.viewmodels.HomeViewModel
import com.example.caesarzonapplication.viewmodels.ShoppingCartViewModel
import com.example.caesarzonapplication.viewmodels.FollowersAndFriendsViewModel
import com.example.caesarzonapplication.viewmodels.WishlistViewModel
import com.example.caesarzonapplication.viewmodels.UserNotificationViewModel


@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    var logged by rememberSaveable { mutableStateOf(true) }
    var showLoginDialog by rememberSaveable { mutableStateOf(false) }
    val isAdmin by rememberSaveable { mutableStateOf(true) }


    Scaffold (
        topBar = {},
        bottomBar = { NavigationBottomBar(navController, logged)  },
        content = { padding ->
            if (showLoginDialog) {
                LoginPopup(
                    onDismiss = { showLoginDialog = false },
                    onLoginSuccess = {
                        logged = true
                        showLoginDialog = false
                    },
                    navController = navController,
                    accountInfoViewModel = AccountInfoViewModel()
                )
            }

            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") {
                    HomeScreen(
                        padding,
                        homeViewModel = HomeViewModel(),
                        navController = navController,
                        logged = logged,
                        userNotificationViewModel = UserNotificationViewModel()
                    )
                }
                composable("shopcart") {
                    ShoppingCartScreen(
                        padding,
                        shoppingCartViewModel = ShoppingCartViewModel(),
                        homeViewModel = HomeViewModel(),
                        navController = navController,
                        logged = logged
                    )
                }
                composable("wishlists") {
                    WishlistScreen(
                        wishlistViewModel = WishlistViewModel(),
                        navController = navController,
                        logged = logged
                    )
                }
                composable("userInfo") {
                    if (logged) {
                        if (isAdmin) {
                            AdminScreen(padding, adminInfoViewModel = AdminInfoViewModel())
                        } else {
                            AccountScreen(padding, accountInfoViewModel = AccountInfoViewModel())
                        }
                    } else {
                        LaunchedEffect(Unit) {
                            showLoginDialog = true
                        }
                    }
                }
                composable("friendlist") {
                    if (logged) {
                        FriendlistScreen(followersAndFriendsViewModel = FollowersAndFriendsViewModel(), navController)
                    } else {
                        LaunchedEffect(Unit) {
                            showLoginDialog = true
                        }
                    }
                }
                composable(
                    route = "productDetails/{productName}",
                    arguments = listOf(navArgument("productName") { type = NavType.StringType })
                ) { backStackEntry ->
                    val productName = backStackEntry.arguments?.getString("productName")
                    productName?.let { ProductDetailsScreen(query = it) }
                }
                composable("userpage"){
                    UserPageScreen(navController = navController)
                }
            }
        },
    )

}



