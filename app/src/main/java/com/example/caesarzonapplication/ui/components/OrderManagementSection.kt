package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OrderManagementSection() {
    val orders = listOf(
        "Ordine #1",
        "Ordine #2",
        "Ordine #3",
        "Ordine #4",
        "Ordine #5",
        "Ordine #6",
        "Ordine #7",
        "Ordine #8",
        "Ordine #9",
        "Ordine #10"
    ) // Esempi di ordini

    var expandedOrder by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Cronologia ordini",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )

        LazyColumn {
            items(orders) { order ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedOrder = if (expandedOrder == order) null else order },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = order, modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = if (expandedOrder == order) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                    if (expandedOrder == order) {
                        Text(text = "Dettagli dell'ordine: $order")
                        Text(text = "Prodotto 1: Descrizione, Prezzo, Quantità")
                        Text(text = "Prodotto 2: Descrizione, Prezzo, Quantità")

                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
