package com.example.caesarzonapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.caesarzonapplication.model.database.AppDatabase
import com.example.caesarzonapplication.model.repository.notificationRepository.SupportRepository
import com.example.caesarzonapplication.model.repository.notificationRepository.UserNotificationRepository
import com.example.caesarzonapplication.model.repository.userRepository.AddressRepository
import com.example.caesarzonapplication.model.repository.userRepository.CardRepository
import com.example.caesarzonapplication.model.repository.userRepository.CityDataRepository
import com.example.caesarzonapplication.model.repository.userRepository.FollowerRepository
import com.example.caesarzonapplication.model.repository.userRepository.ProfileImageRepository
import com.example.caesarzonapplication.model.repository.userRepository.UserRepository
import com.example.caesarzonapplication.model.repository.wishlistRepository.WishlistRepository
import com.example.caesarzonapplication.model.service.KeycloakService
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.ShoppingCartViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AccountInfoViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AccountInfoViewModelFactory
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.NotificationViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.ProductsViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.WishlistViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.WishlistViewModelFactory
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AddressViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AddressViewModelFactory
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.CardsViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.CardsViewModelFactory
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.FollowersViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.FollowersViewModelFactory
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.NotificationViewModelFactory
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.OrdersViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.ReviewViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.SupportRequestsViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.SupportRequestsViewModelFactory
import com.example.caesarzonapplication.navigation.DetailsScreen
import com.example.caesarzonapplication.navigation.NavigationGraph
import com.example.caesarzonapplication.ui.screens.MainScreen
import com.example.caesarzonapplication.ui.theme.CaesarzonApplicationTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    // Variabile per tracciare se viene dal pagamento PayPal
    private var isFromPayPal = false
    private var redirectUrl = ""
    // Funzione per gestire l'intent
    private fun handlePayPalRedirect(intent: Intent?) {
        intent?.data?.let { uri ->
            if (uri.scheme == "caesarzon" && uri.host == "payment") {
                isFromPayPal = true
                redirectUrl = uri.query.toString()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handlePayPalRedirect(intent)

        val productsViewModel: ProductsViewModel by viewModels()

        val accountInfoViewModel by viewModels<AccountInfoViewModel>{
             AccountInfoViewModelFactory(UserRepository(AppDatabase.getDatabase(this).userDao()),
                 ProfileImageRepository(AppDatabase.getDatabase(this).profileImageDao())) }

        val addressViewModel by viewModels<AddressViewModel>{ AddressViewModelFactory(AddressRepository(AppDatabase.getDatabase(this).addressDao()), CityDataRepository(AppDatabase.getDatabase(this).cityDataDao())) }

        val cardViewModel by viewModels<CardsViewModel>{ CardsViewModelFactory(CardRepository(AppDatabase.getDatabase(this).cardDao())) }

        val followersViewModel: FollowersViewModel by viewModels<FollowersViewModel>{
            FollowersViewModelFactory(FollowerRepository(AppDatabase.getDatabase(this).followerDao()))}

        val notificationViewModel: NotificationViewModel by viewModels<NotificationViewModel>{
            NotificationViewModelFactory(UserNotificationRepository(AppDatabase.getDatabase(this).userNotificationDao()))
        }

        val supportRequestsViewModel: SupportRequestsViewModel by viewModels<SupportRequestsViewModel>{
            SupportRequestsViewModelFactory(SupportRepository(AppDatabase.getDatabase(this).supportDao()))
        }
        val reviewViewModel: ReviewViewModel by viewModels()

        val wishlistViewModel: WishlistViewModel by viewModels<WishlistViewModel>{
            WishlistViewModelFactory(WishlistRepository(AppDatabase.getDatabase(this).wishlistDao()))
        }
        val ordersViewModel: OrdersViewModel by viewModels()

        val shoppingCartViewModel: ShoppingCartViewModel by viewModels()
        setContent{
            CaesarzonApplicationTheme{
                val navController = rememberNavController()

                KeycloakService().getBasicToken()
                if(isFromPayPal){
                    Payment(navController, productsViewModel, accountInfoViewModel, followersViewModel, addressViewModel, cardViewModel, supportRequestsViewModel, reviewViewModel, wishlistViewModel, notificationViewModel, shoppingCartViewModel, ordersViewModel, isFromPayPal, redirectUrl)
                }else{
                    Loading(navController, productsViewModel, accountInfoViewModel, followersViewModel, addressViewModel, cardViewModel, supportRequestsViewModel, reviewViewModel, wishlistViewModel, notificationViewModel, shoppingCartViewModel, ordersViewModel, isFromPayPal, redirectUrl)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handlePayPalRedirect(intent)
    }
}






@Composable
fun LoadingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Benvenuto su Caesarzon",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun Loading(
    navController: NavHostController,
    productsViewModel: ProductsViewModel,
    accountInfoViewModel: AccountInfoViewModel,
    followersViewModel: FollowersViewModel,
    addressViewModel: AddressViewModel,
    cardViewModel: CardsViewModel,
    supportRequestsViewModel: SupportRequestsViewModel,
    reviewViewModel: ReviewViewModel,
    wishlistViewModel: WishlistViewModel,
    notificationViewModel: NotificationViewModel,
    shoppingCartViewModel: ShoppingCartViewModel,
    ordersViewModel: OrdersViewModel,
    isFromPayPal: Boolean,
    redirectUrl: String
) {
    var isLoading by remember { mutableStateOf(true) }
    LoadingScreen()

    LaunchedEffect(Unit) {
        delay(3000)
        isLoading = false
    }
    if(isLoading) {
        LoadingScreen()

    }else{
        MainScreen(
            navController = navController,
            accountInfoViewModel = accountInfoViewModel,
            productsViewModel = productsViewModel,
            followersViewModel = followersViewModel,
            addressViewModel = addressViewModel,
            cardsViewModel = cardViewModel,
            notificationViewModel = notificationViewModel,
            supportRequestsViewModel = supportRequestsViewModel,
            reviewViewModel = reviewViewModel,
            wishlistViewModel = wishlistViewModel,
            shoppingCartViewModel = shoppingCartViewModel,
            ordersViewModel = ordersViewModel,
            isFromPayPal = isFromPayPal,
            redirectUrl = redirectUrl
        )
    }
}

@Composable
fun Payment(
    navController: NavHostController,
    productsViewModel: ProductsViewModel,
    accountInfoViewModel: AccountInfoViewModel,
    followersViewModel: FollowersViewModel,
    addressViewModel: AddressViewModel,
    cardViewModel: CardsViewModel,
    supportRequestsViewModel: SupportRequestsViewModel,
    reviewViewModel: ReviewViewModel,
    wishlistViewModel: WishlistViewModel,
    notificationViewModel: NotificationViewModel,
    shoppingCartViewModel: ShoppingCartViewModel,
    ordersViewModel: OrdersViewModel,
    isFromPayPal: Boolean,
    redirectUrl: String
) {

    MainScreen(
        navController = navController,
        accountInfoViewModel = accountInfoViewModel,
        productsViewModel = productsViewModel,
        followersViewModel = followersViewModel,
        addressViewModel = addressViewModel,
        cardsViewModel = cardViewModel,
        notificationViewModel = notificationViewModel,
        supportRequestsViewModel = supportRequestsViewModel,
        reviewViewModel = reviewViewModel,
        wishlistViewModel = wishlistViewModel,
        shoppingCartViewModel = shoppingCartViewModel,
        ordersViewModel = ordersViewModel,
        isFromPayPal = isFromPayPal,
        redirectUrl = redirectUrl
    )

}