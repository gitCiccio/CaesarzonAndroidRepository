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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.caesarzonapplication.ui.components.AppTopBar
import com.example.caesarzonapplication.ui.components.CategoryGrid
import com.example.caesarzonapplication.ui.components.HorizontalProductSection
import com.example.caesarzonapplication.ui.components.NavigationBottomBar
import com.example.caesarzonapplication.ui.components.NotificationFloatingButton
import com.example.caesarzonapplication.ui.components.NotificationsPopup
import com.example.caesarzonapplication.viewmodels.HomeViewModel
import kotlin.math.log

@Composable
fun HomeScreen(paddingValues: PaddingValues, homeViewModel: HomeViewModel, navController: NavHostController, logged : Boolean){
    var showNotificationsPopup by rememberSaveable { mutableStateOf(false) }
    val notifications = listOf("Notifica 1", "Notifica 2", "Notifica 3")
    Scaffold(
        topBar = { Column {
                Spacer(modifier = Modifier.height(45.dp))
                AppTopBar(navController)
        } },
        bottomBar = { NavigationBottomBar(navController = rememberNavController(), logged = true)},
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
                        HorizontalProductSection(title ="Offerte speciali", products = homeViewModel.products, navController)
                        HorizontalProductSection(title = "Novità", products = homeViewModel.products, navController)
                    }
                }
            }
            if (showNotificationsPopup) {
                NotificationsPopup(notifications = notifications, onDismissRequest = { showNotificationsPopup = false })
            }
        }
    )
}




