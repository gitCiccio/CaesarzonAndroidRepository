package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.model.viewmodels.ProductsViewModel
import com.example.caesarzonapplication.ui.components.CategoryGrid
import com.example.caesarzonapplication.ui.components.HorizontalProductSection

@Composable
fun HomeScreen(
    navController: NavHostController,
    productsViewModel: ProductsViewModel,
){

    LaunchedEffect(Unit) {
        productsViewModel.loadNewProducts()
        productsViewModel.loadHotProducts()
    }


    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        content = {
            item {
                CategoryGrid(navController)
                HorizontalProductSection(title ="Offerte speciali", products = productsViewModel.hotProducts,navController)
                HorizontalProductSection(title = "Novità", products = productsViewModel.newProducts,navController)
            }
        }
    )
}
