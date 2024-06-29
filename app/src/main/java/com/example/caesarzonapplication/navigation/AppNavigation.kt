package com.example.caesarzonapplication.navigation


import ShoppingCartScreen
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
import com.example.caesarzonapplication.ui.components.AdminNavigationBottomBar
import com.example.caesarzonapplication.ui.components.LoginPopup
import com.example.caesarzonapplication.ui.components.NavigationBottomBar
import com.example.caesarzonapplication.ui.screens.AccountScreen
import com.example.caesarzonapplication.ui.screens.AdminScreen
import com.example.caesarzonapplication.ui.screens.FriendlistScreen

import com.example.caesarzonapplication.ui.screens.*
import com.example.caesarzonapplication.viewmodels.*
import com.example.caesarzonapplication.viewmodels.AdminViewModels.ReportViewModel
import com.example.caesarzonapplication.viewmodels.AdminViewModels.SearchUsersViewModel
import com.example.caesarzonapplication.viewmodels.AdminViewModels.SupportRequestViewModel


@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    var logged by rememberSaveable { mutableStateOf(false) }
    var showLoginDialog by rememberSaveable { mutableStateOf(false) }
    val isAdmin by rememberSaveable { mutableStateOf(true) }


    Scaffold (
        topBar = {},
        bottomBar = {
            if (logged) {
                if (isAdmin) {
                    AdminNavigationBottomBar(navController)
                } else {
                    NavigationBottomBar(navController, logged)
                }
            } else {
                NavigationBottomBar(navController, logged)
            }
        },
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
                        userNotificationViewModel = UserNotificationViewModel(),
                        isAdmin = isAdmin
                    )
                }
                composable("shopcart") {
                    ShoppingCartScreen(
                        padding,
                        shoppingCartViewModel = ShoppingCartViewModel(),
                        homeViewModel = HomeViewModel(),
                        navController = navController,
                        logged = logged,

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
                            AdminScreen(padding, banViewModel = BanViewModel())
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
                    productName?.let { ProductDetailsScreen(query = it, navController) }
                }
                composable("userpage"){
                    UserPageScreen(navController = navController)
                }
                composable("searchUser") {
                    UserSearchScreen(SearchUsersViewModel())
                }
                composable("reports") {
                    ReportsScreen(ReportViewModel(), navController)
                }
                composable("supportRequest") {
                    SupportRequestScreen(SupportRequestViewModel(), navController)
                }
            }
        },
    )

}



