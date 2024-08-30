package com.example.caesarzonapplication.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.isAdmin
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.logged
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AccountInfoViewModel
import com.example.caesarzonapplication.model.viewmodels.adminViewModels.*
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.ProductsViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.WishlistViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AddressViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.CardsViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.FollowersViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.NotificationViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.ReviewViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.SupportRequestsViewModel
import com.example.caesarzonapplication.ui.screens.AccountScreen
import com.example.caesarzonapplication.ui.screens.adminScreens.AddProductScreen
import com.example.caesarzonapplication.ui.screens.AuthScreen
import com.example.caesarzonapplication.ui.screens.FriendlistScreen
import com.example.caesarzonapplication.ui.screens.HomeScreen
import com.example.caesarzonapplication.ui.screens.ProductDetailsScreen
//import com.example.caesarzonapplication.ui.screens.ShoppingCartScreen
import com.example.caesarzonapplication.ui.screens.WishlistScreen
import com.example.caesarzonapplication.ui.screens.adminScreens.AdminScreen
import com.example.caesarzonapplication.ui.screens.ProductSearchResultsScreen
import com.example.caesarzonapplication.ui.screens.adminScreens.ReportsScreen
import com.example.caesarzonapplication.ui.screens.UserPageScreen
import com.example.caesarzonapplication.ui.screens.UserRegistrationScreen
import com.example.caesarzonapplication.ui.screens.adminScreens.SupportRequestScreen
import com.example.caesarzonapplication.ui.screens.adminScreens.UserSearchScreen
import java.util.UUID

@Composable
fun NavigationGraph(
    navController: NavHostController,
    productsViewModel: ProductsViewModel,
    accountInfoViewModel: AccountInfoViewModel,
    followerViewModel: FollowersViewModel,
    addressViewModel: AddressViewModel,
    cardViewModel: CardsViewModel,
    supportRequestViewModel: SupportRequestsViewModel,
    reviewViewModel: ReviewViewModel,
    wishlistViewModel: WishlistViewModel,
    notificationViewModel: NotificationViewModel
) {
    NavHost(navController, startDestination = BottomBarScreen.Home.route) {

        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(navController, productsViewModel, notificationViewModel)
        }

        composable(route = BottomBarScreen.Profile.route) {
            if(logged.value)
                AccountScreen(navController, accountInfoViewModel, addressViewModel, supportRequestViewModel, cardViewModel, notificationViewModel)
            else AuthScreen(navController,accountInfoViewModel,followerViewModel)
        }

        composable(route = BottomBarScreen.Cart.route) {
            //ShoppingCartScreen(navController,logged, productsViewModel)
        }

        composable(route = BottomBarScreen.Friends.route) {
            if(logged.value) {
                FriendlistScreen(navController, notificationViewModel, followerViewModel)
            }
            else AuthScreen(navController, accountInfoViewModel,followerViewModel)
        }

        composable(route = BottomBarScreen.Wishlist.route) {
            if(logged.value)
                WishlistScreen(navController, notificationViewModel, wishlistViewModel)
            else AuthScreen(navController, accountInfoViewModel,followerViewModel)
        }

        if(isAdmin.value) {
            val searchAndBanUsersViewModel = SearchAndBanUsersViewModel()
            val reportViewModel = AdminReportViewModel()
            val adminSupportRequestViewModel = AdminSupportRequestViewModel()
            val adminProductViewModel = AdminProductViewModel()

            composable(route = AdminBottomBarScreen.Home.route) {
                AdminScreen(navController, productsViewModel)
            }
            composable(route = AdminBottomBarScreen.AddProduct.route) {
                AddProductScreen(adminProductViewModel)
            }
            composable(route = AdminBottomBarScreen.Reports.route) {
                ReportsScreen(reportViewModel)
            }
            composable(route = AdminBottomBarScreen.SupportRequest.route) {
                SupportRequestScreen(adminSupportRequestViewModel)
            }
            composable(route = AdminBottomBarScreen.SearchUser.route) {
                UserSearchScreen(searchAndBanUsersViewModel)
            }
        }


        composable(
            route = DetailsScreen.ProductDetailsScreen.route+"/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            if (productId != null) {
                    val uuid = UUID.fromString(productId)
                    ProductDetailsScreen(productID = uuid, navController, productsViewModel,reviewViewModel, wishlistViewModel)
            } else {
                Log.e("NavigationError", "productId is null")
            }
        }

        composable(route = DetailsScreen.ProductSearchResultsScreen.route+"/{parameter}",
            arguments = listOf(navArgument("parameter") { type = NavType.StringType })
        ) { backStackEntry ->
            val parameter = backStackEntry.arguments?.getString("parameter") ?: ""
            if (parameter.isNotEmpty())
                ProductSearchResultsScreen(parameter, productsViewModel, navController)
        }

        composable(route = DetailsScreen.UserPageDetailsScreen.route) {
            UserPageScreen()
        }

        composable(route = DetailsScreen.UserRegistrationDetailsScreen.route) {
            UserRegistrationScreen(navController, accountInfoViewModel)
        }

    }
}