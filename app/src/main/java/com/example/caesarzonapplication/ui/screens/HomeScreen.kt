package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.caesarzonapplication.ui.components.AppTopBar
import com.example.caesarzonapplication.ui.components.HorizontalProductSection
import com.example.caesarzonapplication.ui.components.NavigationBottomBar
import com.example.caesarzonapplication.viewmodels.HomeViewModel

@Composable
fun HomeScreen(paddingValues: PaddingValues, homeViewModel: HomeViewModel){
    Scaffold(
        topBar = { Column {
                Spacer(modifier = Modifier.height(40.dp))
                AppTopBar()
        } },
        bottomBar = { NavigationBottomBar(navController = rememberNavController())},
        content = { padding ->
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(top=20.dp)
                    .background(Color(247, 170, 76, 255)
                    )
            ){
                Spacer(modifier = Modifier.height(5.dp))
                HorizontalProductSection(title ="Offerte speciali", products = homeViewModel.products)
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalProductSection(title = "Novità", products = homeViewModel.products)

            }
        }
    )
}




