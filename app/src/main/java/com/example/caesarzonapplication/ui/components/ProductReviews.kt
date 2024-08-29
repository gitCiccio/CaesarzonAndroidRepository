package com.example.caesarzonapplication.ui.components

import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.sharp.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.model.dto.ReviewDTO
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.isAdmin
import com.example.caesarzonapplication.model.viewmodels.ProductsViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.ReviewViewModel
import java.util.UUID

@Composable
fun ProductReviews(navController : NavHostController, reviewViewModel: ReviewViewModel, productId: String) {
    var isReviewExpanded by remember { mutableStateOf(false) }
    var isAddReviewDialogOpen by remember { mutableStateOf(false) }
    val reviews = remember { mutableStateListOf<ReviewDTO>() }
    LaunchedEffect(Unit) {
        reviewViewModel.getAllProductReviews(productId)
    }

    if(!isAdmin.value){
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isReviewExpanded = !isReviewExpanded },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Recensioni",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Icon(
                    imageVector = if (isReviewExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Espandi/Comprimi Recensioni",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            if (isReviewExpanded) {
                //lista fittizia, da rimpiazzare
                Column {
                    reviews.forEach { review ->
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Data: ${review.localDate}",
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
                        }
                    }
                }
            }
        }
        Button(
            onClick = { isAddReviewDialogOpen = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Aggiungi Recensione")
        }
        if (isAddReviewDialogOpen) {
            AddReviewPopup(
                onDismiss = { isAddReviewDialogOpen = false },
                onAddReview = {
                    /*val review = ReviewDTO()
                    reviewViewModel.addReview() */},
                navController
            )
        }
    }
}
