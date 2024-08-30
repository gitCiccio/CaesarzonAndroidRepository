package com.example.caesarzonapplication.ui.screens

import ProductActions
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.R
import com.example.caesarzonapplication.model.dto.productDTOS.ReviewDTO
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.logged
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.ProductsViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.WishlistViewModel
import com.example.caesarzonapplication.model.viewmodels.adminViewModels.AdminProductViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.ReviewViewModel
import com.example.caesarzonapplication.ui.components.ProductReviews
import java.time.LocalDate
import java.util.UUID

@Composable
fun ProductDetailsScreen(
    productID: UUID,
    navController: NavHostController,
    productsViewModel: ProductsViewModel,
    reviewViewModel: ReviewViewModel,
    wishlistViewModel: WishlistViewModel
) {

    var isAddReviewDialogOpen by remember { mutableStateOf(false) }
    val adminProductViewModel = AdminProductViewModel()
    var selectedProduct by remember { mutableStateOf(productsViewModel.selectedProduct) }

    LaunchedEffect(Unit) {
        productsViewModel.getProduct(productID)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                selectedProduct.value.let {
                    if (it != null) {
                        Text(text = it.product.sport  ,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.align(Alignment.Start)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                selectedProduct.value.let {
                    if (it != null) {
                        Text(
                            text = it.product.name,
                            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp),
                            modifier = Modifier .align(Alignment.CenterHorizontally)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                val imageBitmap = selectedProduct.value?.image?.asImageBitmap()
                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = selectedProduct.value?.product?.name,
                        modifier = Modifier
                            .size(200.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .align(Alignment.CenterHorizontally)
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "immagine_prodotto_non_disponibile",
                        modifier = Modifier
                            .size(200.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .align(Alignment.CenterHorizontally)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Price: \$${selectedProduct.value?.product?.price}",
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
                selectedProduct.value.let {
                    if (it != null) {
                        Text(
                            text = it.product.description,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .align(Alignment.Start)
                                .fillMaxWidth(0.9f)
                        )
                    }
                }
            }
        }
        item {
            selectedProduct.value.let { selectedProduct.value?.let { it1 -> ProductActions(navController, adminProductViewModel, wishlistViewModel,it1.product) } }
        }
        item {
            if(logged.value) {
                Button(
                    onClick = { isAddReviewDialogOpen = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = "Aggiungi Recensione")
                }
            }
            if (isAddReviewDialogOpen) {

                var evaluation by remember { mutableIntStateOf(0) }
                var text by remember { mutableStateOf("") }

                AlertDialog(
                    onDismissRequest = { isAddReviewDialogOpen = false },
                    confirmButton = {
                        Button(
                            onClick = {
                                val newReview = ReviewDTO(
                                    id = UUID.randomUUID(),
                                    text = text,
                                    evaluation = evaluation,
                                    username = "",
                                    productID = productID,
                                    date = LocalDate.now().toString(),
                                )
                                reviewViewModel.addReview(newReview)
                                navController.navigate("productDetails/${selectedProduct.value?.product?.id}")
                                isAddReviewDialogOpen = false
                            }
                        ) {
                            Text(text = "Aggiungi")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { isAddReviewDialogOpen = false }) {
                            Text(text = "Annulla")
                        }
                    },
                    title = {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize()
                                .height(40.dp),
                            text = "Aggiungi una recensione"
                        )
                    },
                    text = {
                        Column {
                            Text(text = "Valutazione: ")
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                for (i in 1..5) {
                                    val iconColor = if (i <= evaluation) Color.Yellow else Color.Gray
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = null,
                                        tint = iconColor,
                                        modifier = Modifier
                                            .clickable { evaluation = i }
                                            .padding(end = 4.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            TextField(
                                value = text,
                                onValueChange = { text = it },
                                label = { Text("Recensione") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp),
                                maxLines = 5
                            )
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            ProductReviews(navController, reviewViewModel, productID.toString())
        }
    }
}