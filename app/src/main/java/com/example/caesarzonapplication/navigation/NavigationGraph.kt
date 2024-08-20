package com.example.caesarzonapplication.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.caesarzonapplication.model.viewmodels.AccountInfoViewModel
import com.example.caesarzonapplication.model.viewmodels.NotificationViewModel
import com.example.caesarzonapplication.model.viewmodels.ProductsViewModel
import com.example.caesarzonapplication.ui.screens.AccountScreen
import com.example.caesarzonapplication.ui.screens.AddProductScreen
import com.example.caesarzonapplication.ui.screens.AuthScreen
import com.example.caesarzonapplication.ui.screens.FriendlistScreen
import com.example.caesarzonapplication.ui.screens.HomeScreen
import com.example.caesarzonapplication.ui.screens.ProductDetailsScreen
import com.example.caesarzonapplication.ui.screens.ShoppingCartScreen
import com.example.caesarzonapplication.ui.screens.WishlistScreen
import com.example.caesarzonapplication.ui.screens.AdminScreen
import com.example.caesarzonapplication.ui.screens.ProductSearchResultsScreen
import com.example.caesarzonapplication.ui.screens.ReportsScreen
import com.example.caesarzonapplication.ui.screens.SupportRequestScreen
import com.example.caesarzonapplication.ui.screens.UserPageScreen
import com.example.caesarzonapplication.ui.screens.UserRegistrationScreen
import java.util.UUID

@Composable
fun NavigationGraph(
    navController: NavHostController,
    isAdmin: MutableState<Boolean>,
    logged: MutableState<Boolean>,
    productsViewModel: ProductsViewModel,
    accountInfoViewModel: AccountInfoViewModel,
    notificationViewModel: NotificationViewModel
) {
    NavHost(navController, startDestination = BottomBarScreen.Home.route) {

        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(navController, productsViewModel)
        }

        composable(route = BottomBarScreen.Profile.route) {
            if(logged.value)
                AccountScreen(navController)
            else AuthScreen(navController,accountInfoViewModel)
        }

        composable(route = BottomBarScreen.Cart.route) {
            ShoppingCartScreen(navController,logged, productsViewModel)
        }

        composable(route = BottomBarScreen.Friends.route) {
            if(logged.value)
                FriendlistScreen(navController)
            else AuthScreen(navController,accountInfoViewModel)
        }

        composable(route = BottomBarScreen.Wishlist.route) {
            if(logged.value)
                WishlistScreen(navController)
            else AuthScreen(navController,accountInfoViewModel)
        }


        composable(route = DetailsScreen.AddProductDetailsScreen.route) {
            AddProductScreen()
        }

        composable(
            route = DetailsScreen.ProductDetailsScreen.route + "/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = UUID.fromString(backStackEntry.arguments?.getString("productId"))
            if (productId != null)
                ProductDetailsScreen(productID = productId, navController, productsViewModel)
        }

        composable(route = DetailsScreen.ProductSearchResultsScreen.route+"/{query}",
            arguments = listOf(navArgument("query") { type = NavType.StringType })
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            if (query != "")
                ProductSearchResultsScreen(query, productsViewModel, navController)
        }

        composable(route = DetailsScreen.ProductSearchResultsScreen.route+"/{category}",
            arguments = listOf(navArgument("category"){ type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            if (category != "")
                ProductSearchResultsScreen(category, productsViewModel, navController)
        }

        composable(route = DetailsScreen.ReportsDetailsScreen.route) {
            ReportsScreen()
        }

        composable(route = DetailsScreen.SupportRequestDetailsScreen.route) {
            SupportRequestScreen()
        }

        composable(route = DetailsScreen.UserPageDetailsScreen.route) {
            UserPageScreen()
        }

        if(isAdmin.value) {
            composable(route = DetailsScreen.AdminScreen.route) {
                AdminScreen()
            }
        }

        composable(route = DetailsScreen.UserRegistrationDetailsScreen.route) {
            UserRegistrationScreen(navController, accountInfoViewModel)
        }


    }
}