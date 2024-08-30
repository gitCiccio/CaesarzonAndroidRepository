package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.isAdmin
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.logged
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AccountInfoViewModel.Companion.userData
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.ReviewViewModel

@Composable
fun ProductReviews(navController : NavHostController, reviewViewModel: ReviewViewModel, productId: String) {

    var isReviewExpanded by remember { mutableStateOf(false) }
    val reviews by reviewViewModel.reviews.collectAsState()
    var showConfirmReportDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        reviewViewModel.getAllProductReviews(productId)
    }

    if (showConfirmReportDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmReportDialog = false },
            title = { Text("Segnala Recensione") },
            text = { Text("Sei sicuro di voler segnalare questa recensione?") },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmReportDialog = false
                    }
                ) {
                    Text("Conferma")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showConfirmReportDialog = false
                    }
                ) {
                    Text("Annulla")
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isReviewExpanded = !isReviewExpanded },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Recensioni",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Icon(
                imageVector = if (isReviewExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = "Espandi/Comprimi Recensioni",
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        if (isReviewExpanded) {
            Column {
                for(review in reviews){
                    if (review.username == userData?.username && !isAdmin.value){
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.Start,
                        ) {
                            Text(text = "La tua recensione: ")
                            Text(
                                text = "Data: ${review.date}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Row {
                                for (i in 1..5) {
                                    val iconColor =
                                        if (i <= review.evaluation) Color.Yellow else Color.Gray
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = null,
                                        tint = iconColor,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Utente: ${review.username}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = review.text,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Button(onClick = {
                                reviewViewModel.deleteReview(productId)
                                //navController.navigate("product_details/${productId}")
                            }) {
                                Text(text = "Elimina recensione")
                            }
                        }
                    }
                }
                reviews.forEach { review ->
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(
                            text = "Data: ${review.date}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Row {
                            for (i in 1..5) {
                                val iconColor =
                                    if (i <= review.evaluation) Color.Yellow else Color.Gray
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = iconColor,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Text(
                            text = "Utente: ${review.username}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = review.text,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if(logged.value){
                            Button(
                                onClick = { showConfirmReportDialog = true }
                            )
                            {
                                Text(text = "Segnala recensione")
                            }
                        }
                        if(isAdmin.value){
                            Button(onClick = {
                                reviewViewModel.deleteReview(productId)
                                //navController.navigate("product_details/${review.productID}")
                            })
                            {
                                Text(text = "Elimina recensione")
                            }
                        }
                    }
                }
            }
        }
    }
}