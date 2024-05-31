package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.caesarzonapplication.ui.components.AppTopBar
import com.example.caesarzonapplication.ui.components.NavigationBottomBar

@Composable
fun HomeScreen(paddingValues: PaddingValues){
    Scaffold(
        topBar = { AppTopBar() },
        bottomBar = { NavigationBottomBar(navController = rememberNavController())},
        content = {
            paddingValues ->
            val pad = paddingValues
            Text(text = "Ciao cesare", modifier = Modifier.padding(pad))
        }
    )
}
