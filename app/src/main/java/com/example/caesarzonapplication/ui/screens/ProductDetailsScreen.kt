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
import com.example.caesarzonapplication.R
import com.example.caesarzonapplication.ui.components.ProductReviews
import com.example.caesarzonapplication.model.viewmodels.AdminViewModels.AdminProductViewModel
import com.example.caesarzonapplication.model.viewmodels.ProductsViewModel
import java.util.UUID

@Composable
fun ProductDetailsScreen(productID: UUID, navController: NavHostController, productsViewModel: ProductsViewModel, isAdmin: MutableState<Boolean>) {

    val adminProductViewModel = AdminProductViewModel()
    var selectedProduct by remember { mutableStateOf(productsViewModel.selectedProduct) }

    LaunchedEffect(Unit) {
        productsViewModel.getProduct(productID = productID)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        item {
            Column(modifier = Modifier .fillMaxWidth()) {
                selectedProduct.value?.let {
                    Text(text = it.sport  ,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }
                selectedProduct.value?.let {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp),
                        modifier = Modifier .align(Alignment.CenterHorizontally)
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = selectedProduct.value?.name,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "Price: \$${selectedProduct.value?.price}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp),
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "Descrizione: ",
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 22.sp),
                    modifier = Modifier.align(Alignment.Start)
                )
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
            selectedProduct.value?.let { ProductActions(navController, adminProductViewModel, it, isAdmin) }
        }
        item {
            ProductReviews(navController)
        }
    }
}
