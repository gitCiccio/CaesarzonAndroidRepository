package com.example.caesarzonapplication.ui.screens

import ProductActions
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.caesarzonapplication.R
import com.example.caesarzonapplication.model.dto.ProductSearchDTO
import com.example.caesarzonapplication.ui.components.AppTopBar
import com.example.caesarzonapplication.ui.components.ProductReviews
import com.example.caesarzonapplication.viewmodels.AdminViewModels.AdminProductViewModel
import com.example.caesarzonapplication.viewmodels.ProductsViewModel
import java.util.UUID

@Composable
fun ProductDetailsScreen(productID: UUID, navController: NavHostController, productsViewModel: ProductsViewModel, adminProductViewModel: AdminProductViewModel) {

    var selectedProduct by remember { mutableStateOf(productsViewModel.selectedProduct) }

    LaunchedEffect(Unit) {
        productsViewModel.getProduct(productID = productID)
    }

    Scaffold(
        topBar = { AppTopBar(navController) },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                item {
                    Column(modifier = Modifier .fillMaxWidth()) {
                        selectedProduct.value?.let {
                            Text(text = it.sport  ,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.align(Alignment.Start)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        selectedProduct.value?.let {
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp),
                                modifier = Modifier .align(Alignment.CenterHorizontally)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = selectedProduct.value?.name,
                            modifier = Modifier
                                .size(200.dp)
                                .clip(MaterialTheme.shapes.medium)
                                .align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(16.dp))


                        Text(
                            text = "Price: \$${selectedProduct.value?.price}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp),
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(16.dp))


                        Text(
                            text = "Descrizione: ",
                            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 22.sp),
                            modifier = Modifier.align(Alignment.Start)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        selectedProduct.value?.let {
                            Text(
                                text = it.description,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .fillMaxWidth(0.9f)
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    selectedProduct.value?.let { ProductActions(navController, adminProductViewModel, it) }
                }
                item{
                    Spacer(modifier = Modifier.height(24.dp))
                }
                item {
                    ProductReviews(navController)
                }
            }
        },
        bottomBar = {Spacer(modifier = Modifier.padding(60.dp)) }
    )
}
