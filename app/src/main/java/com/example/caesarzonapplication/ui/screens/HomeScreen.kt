package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.ui.components.AppTopBar
import com.example.caesarzonapplication.ui.components.CategoryGrid
import com.example.caesarzonapplication.ui.components.HorizontalProductSection
import com.example.caesarzonapplication.ui.components.NotificationFloatingButton
import com.example.caesarzonapplication.ui.components.NotificationsPopup
import com.example.caesarzonapplication.model.viewmodels.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(paddingValues: PaddingValues, homeViewModel: HomeViewModel, navController: NavHostController, logged : Boolean, isAdmin: Boolean){
    var showNotificationsPopup by rememberSaveable { mutableStateOf(false) }
    val newProducts by homeViewModel.newProducts.collectAsState()
    val hotProducts by homeViewModel.hotProducts.collectAsState()
    val adminNotifications by homeViewModel.adminNotification.collectAsState()
    val userNotifications by homeViewModel.userNotification.collectAsState()

    LaunchedEffect(Unit) {
        homeViewModel.loadProducts()
    }


    Scaffold(
        topBar = { Column {
                Spacer(modifier = Modifier.height(45.dp))
                AppTopBar(navController)
        } },
        bottomBar = { Spacer(modifier = Modifier.padding(60.dp))
        /*if(!isAdmin) NavigationBottomBar(navController = rememberNavController(), logged = true) else AdminNavigationBottomBar(navController = rememberNavController())*/ },
        floatingActionButton = {
            if(logged){
                NotificationFloatingButton(onClick =  {
                    showNotificationsPopup = true
                })
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        content = { padding ->
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.White)
            ){
                item{
                    Column {
                        CategoryGrid()
                        HorizontalProductSection(title ="Offerte speciali", products = newProducts, navController)
                        HorizontalProductSection(title = "Novità", products = hotProducts, navController)
                    }
                }
            }
            if (showNotificationsPopup) {
                val notificationsToShow = if (isAdmin) adminNotifications else userNotifications
                NotificationsPopup(
                    notifications = notificationsToShow,
                    onDismissRequest = { showNotificationsPopup = false },
                    homeViewModel
                    )
            }
        }
    )
}






