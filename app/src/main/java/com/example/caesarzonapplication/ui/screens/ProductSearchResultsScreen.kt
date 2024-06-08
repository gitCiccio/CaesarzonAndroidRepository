package com.example.caesarzonapplication.ui.screens

import android.app.appsearch.SearchResult
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.caesarzonapplication.ui.components.AppTopBar
import com.example.caesarzonapplication.ui.components.NavigationBottomBar
import com.example.caesarzonapplication.viewmodels.ProductsViewModel
/*
@Composable
fun ProductSearchResultScreen(query: String, productsViewModel: ProductsViewModel, navController: NavHostController){
    val filteredResults = productsViewModel.loadSimpleProducts()
    Scaffold (
        topBar = { AppTopBar(navController)},
        bottomBar = { NavigationBottomBar(navController = rememberNavController(), logged = false)},
            content = {

                ProductsList(productsViewModel.loadSimpleProducts())


            }
    )
}

 */