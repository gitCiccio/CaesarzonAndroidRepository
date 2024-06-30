package com.example.caesarzonapplication.ui.components

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.model.dto.ReviewDTO
import java.util.UUID

@Composable
fun ProductReviews(navController : NavHostController) {
    var isReviewExpanded by remember { mutableStateOf(false) }
    var isAddReviewDialogOpen by remember { mutableStateOf(false) }

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

            val reviews = listOf(
                ReviewDTO(
                    id = UUID.randomUUID(),
                    text = "Ottimo prodotto! La qualità è superiore alle aspettative e il prezzo è molto conveniente.",
                    evaluation = 5,
                    username = "user1",
                    productID = UUID.randomUUID(),
                    localDate = "2024-06-01"
                ),
                ReviewDTO(
                    id = UUID.randomUUID(),
                    text = "Molto utile e ben fatto. L'unica pecca è la spedizione che ha tardato un po'.",
                    evaluation = 4,
                    username = "user2",
                    productID = UUID.randomUUID(),
                    localDate = "2024-06-05"
                ),
                ReviewDTO(
                    id = UUID.randomUUID(),
                    text = "Buon rapporto qualità/prezzo. Tuttavia, il materiale potrebbe essere migliore.",
                    evaluation = 3,
                    username = "user3",
                    productID = UUID.randomUUID(),
                    localDate = "2024-06-10"
                ),
                ReviewDTO(
                    id = UUID.randomUUID(),
                    text = "Non sono molto soddisfatto. Il prodotto non è come descritto.",
                    evaluation = 2,
                    username = "user4",
                    productID = UUID.randomUUID(),
                    localDate = "2024-06-15"
                ),
                ReviewDTO(
                    id = UUID.randomUUID(),
                    text = "Pessima qualità. Si è rotto dopo una settimana di utilizzo.",
                    evaluation = 1,
                    username = "user5",
                    productID = UUID.randomUUID(),
                    localDate = "2024-06-20"
                )
            )
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
                                val iconColor = if (i <= review.evaluation) Color.Yellow else Color.Gray
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = iconColor,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Utente: ${review.username}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = review.text,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }

    Button(
        onClick = { isAddReviewDialogOpen = true},
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ){
        Text(text = "Aggiungi Recensione")
    }
    if(isAddReviewDialogOpen){
        AddReviewPopup(
            onDismiss = { isAddReviewDialogOpen = false },
            onAddReview = { /* Aggiungi la nuova recensione */ },
            navController
        )
    }
}
