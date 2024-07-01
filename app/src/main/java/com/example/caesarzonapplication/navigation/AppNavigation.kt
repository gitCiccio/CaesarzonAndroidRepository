package com.example.caesarzonapplication.navigation

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
import com.example.caesarzonapplication.model.dto.SendProductDTO
import com.example.caesarzonapplication.model.repository.NotifyRepository
import com.example.caesarzonapplication.model.repository.ProductRepository
import com.example.caesarzonapplication.ui.components.AdminNavigationBottomBar
import com.example.caesarzonapplication.ui.components.LoginPopup
import com.example.caesarzonapplication.ui.components.NavigationBottomBar
import com.example.caesarzonapplication.ui.screens.*
import com.example.caesarzonapplication.viewmodels.*
import com.example.caesarzonapplication.viewmodels.AdminViewModels.AdminProductViewModel
import com.example.caesarzonapplication.viewmodels.AdminViewModels.ReportViewModel
import com.example.caesarzonapplication.viewmodels.AdminViewModels.SearchAndBanUsersViewModel
import com.example.caesarzonapplication.viewmodels.AdminViewModels.SupportRequestViewModel
import com.google.gson.Gson
import java.util.UUID


@Composable
fun AppNavigation() {
    val gson = Gson()
    val navController = rememberNavController()
    var logged by rememberSaveable { mutableStateOf(false) }
    var showLoginDialog by rememberSaveable { mutableStateOf(false) }
    val isAdmin by rememberSaveable { mutableStateOf(true) }
    val productRepository = ProductRepository()
    val notificationRepository = NotifyRepository()
    val productsViewModel = ProductsViewModel()
    val adminProductViewModel = AdminProductViewModel()

    Scaffold(
        topBar = {},
        bottomBar = {
            if (logged && isAdmin){
                AdminNavigationBottomBar(navController)}
            else
                NavigationBottomBar(navController, logged)
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
                        homeViewModel = HomeViewModel(productRepository, notificationRepository, isAdmin),
                        navController = navController,
                        logged = logged,
                        isAdmin = isAdmin,

                    )
                }
                composable("shopcart") {
                    ShoppingCartScreen(
                        padding,
                        shoppingCartViewModel = ShoppingCartViewModel(),
                        navController = navController,
                        logged = logged
                    )
                }
                composable("register") {
                    UserRegistrationScreen(
                        padding,
                        navController = navController,
                        logged = logged
                    )
                }
                composable("wishlists") {
                    if (logged){
                        WishlistScreen(
                            wishlistViewModel = WishlistViewModel(),
                            navController = navController,
                            logged = logged
                        )}
                    else{
                        LaunchedEffect(Unit) {
                            showLoginDialog = true
                        }
                    }
                }
                composable(
                    route = "addProduct/{product}",
                    arguments = listOf(navArgument("product") { type = NavType.StringType })
                ) { backStackEntry ->
                    val productJson = backStackEntry.arguments?.getString("product")
                    val productDTO = productJson?.let {
                        // Deserializza il prodotto da JSON a ProductDTO
                        gson.fromJson(it, SendProductDTO::class.java)
                    }
                    AddProductScreen(adminProductViewModel = adminProductViewModel, productDTO = productDTO)
                }
                composable("userInfo") {
                    if (logged && !isAdmin) {
                        AccountScreen(padding, accountInfoViewModel = AccountInfoViewModel())
                    } else {
                        LaunchedEffect(Unit) {
                            showLoginDialog = true
                        }
                    }
                }
                composable("friendlist") {
                    if (logged) {
                        FriendlistScreen(FollowersAndFriendsViewModel(), navController)
                    } else {
                        LaunchedEffect(Unit) {
                            showLoginDialog = true
                        }
                    }
                }
                composable(
                    route = "productDetails/{productId}",
                    arguments = listOf(navArgument("productId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId")
                    productId?.let { ProductDetailsScreen(UUID.fromString(productId), navController, productsViewModel,adminProductViewModel) }
                }
                composable("userpage") {
                    UserPageScreen(navController = navController)
                }
                composable("searchUser") {
                    UserSearchScreen(SearchAndBanUsersViewModel())
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



