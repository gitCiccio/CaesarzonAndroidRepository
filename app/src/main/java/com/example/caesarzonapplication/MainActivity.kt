package com.example.caesarzonapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.caesarzonapplication.model.database.AppDatabase
import com.example.caesarzonapplication.model.repository.notificationRepository.UserNotificationRepository
import com.example.caesarzonapplication.model.repository.userRepository.AddressRepository
import com.example.caesarzonapplication.model.repository.userRepository.CardRepository
import com.example.caesarzonapplication.model.repository.userRepository.CityDataRepository
import com.example.caesarzonapplication.model.repository.userRepository.FollowerRepository
import com.example.caesarzonapplication.model.repository.userRepository.ProfileImageRepository
import com.example.caesarzonapplication.model.repository.userRepository.UserRepository
import com.example.caesarzonapplication.model.service.KeycloakService
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AccountInfoViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AccountInfoViewModelFactory
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.NotificationViewModel
import com.example.caesarzonapplication.model.viewmodels.ProductsViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AddressViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AddressViewModelFactory
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.CardsViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.CardsViewModelFactory
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.FollowersViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.FollowersViewModelFactory
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.NotificationViewModelFactory
import com.example.caesarzonapplication.ui.screens.MainScreen
import com.example.caesarzonapplication.ui.theme.CaesarzonApplicationTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
         val wishlistViewModel: ProductsViewModel by viewModels()
         val friendsViewModel: ProductsViewModel by viewModels()

        setContent{
            CaesarzonApplicationTheme{
                val navController = rememberNavController()
                KeycloakService().getBasicToken()

                MainScreen(
                    navController = navController,
                    accountInfoViewModel = accountInfoViewModel,
                    productsViewModel = productsViewModel,
                    followersViewModel = followersViewModel,
                    addressViewModel = addressViewModel,
                    cardsViewModel = cardViewModel,
                    notificationViewModel = notificationViewModel,
                )
            }
        }
    }
}


