package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OrderManagementSection() {
    val orders = listOf("Ordine #1", "Ordine #2", "Ordine #3") // Dati fittizi

    Column {
        Text(text = "Cronologia ordini", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))

        orders.forEach { order ->
            Text(text = order, modifier = Modifier.padding(8.dp))
        }
    }
}
