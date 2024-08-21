package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.model.service.KeycloakService
import com.example.caesarzonapplication.model.viewmodels.AccountInfoViewModel
import com.example.caesarzonapplication.model.viewmodels.NotificationViewModel
import com.example.caesarzonapplication.model.viewmodels.ProductsViewModel
import com.example.caesarzonapplication.navigation.AdminNavigationBottomBar
import com.example.caesarzonapplication.navigation.NavigationBottomBar
import com.example.caesarzonapplication.navigation.NavigationGraph
import com.example.caesarzonapplication.ui.components.SearchBar

@Composable
fun MainScreen(
    navController: NavHostController,
    isAdmin: MutableState<Boolean>,
    logged: MutableState<Boolean>,
    accountInfoViewModel: AccountInfoViewModel,
    productsViewModel: ProductsViewModel,
    notificationViewModel: NotificationViewModel
){

    var showNotificationsPopup by rememberSaveable { mutableStateOf(false) }
    val userNotifications by notificationViewModel.userNotification.collectAsState()

    Scaffold(
        topBar = { SearchBar(navController) },
        bottomBar =
        {
            if(!isAdmin.value)
                NavigationBottomBar(navController)
            else
                AdminNavigationBottomBar(navController)
        },
        floatingActionButton = {
            if(logged.value){
                FloatingActionButton(
                    onClick =  { showNotificationsPopup = !showNotificationsPopup },
                    containerColor = Color(100,104,208),
                    contentColor = Color(238, 137, 60),
                    shape = CircleShape,
                    content = {
                        Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
                    },
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .padding(end = 16.dp)
                )
                if (userNotifications.isNotEmpty()) {
                    Text(
                        text = "${userNotifications.size}",
                        color = Color.White,
                        modifier = Modifier
                            .background(Color.Red, shape = CircleShape)
                            .padding(end = 16.dp)
                            .padding(bottom = 22.dp),
                    )
                }
            } },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(padding),
            ) {
                NavigationGraph(
                    navController =navController,
                    isAdmin = isAdmin,
                    logged = logged,
                    productsViewModel = productsViewModel,
                    accountInfoViewModel = accountInfoViewModel,
                )
            }
        }
    )
}