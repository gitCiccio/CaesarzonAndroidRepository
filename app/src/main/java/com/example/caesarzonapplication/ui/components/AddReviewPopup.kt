package com.example.caesarzonapplication.ui.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.model.dto.ReviewDTO
import java.util.UUID

@Composable
fun AddReviewPopup(onDismiss: () -> Unit, onAddReview: (ReviewDTO) -> Unit, navController: NavHostController) {
    var evaluation by remember { mutableStateOf(0) }
    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    //da vedere se implementare qui o nel ProductDetailsScreen
                    val newReview = ReviewDTO(
                        id = UUID.randomUUID(),
                        text = text,
                        evaluation = evaluation,
                        username = "currentUserName",
                        productID = UUID.randomUUID(),
                        localDate = "2024-06-29"
                    )
                    onAddReview(newReview)
                    navController.navigate("productDetails/{productName}")
                    onDismiss()
                }
            ) {
                Text(text = "Aggiungi")
            }
        },
        dismissButton = {
            Button(onClick = {
                navController.navigate("productDetails/{productName}")
                onDismiss()
            }) {
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
                Text(text = "Valutazione:")
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