package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun ReturnsSection() {
    val orders = listOf("Ordine #1", "Ordine #2", "Ordine #3") // Dati fittizi
    var showReturnDialog by rememberSaveable { mutableStateOf(false) }
    var selectedOrder by rememberSaveable { mutableStateOf("") }
    var returnReason by rememberSaveable { mutableStateOf(TextFieldValue("")) }

    Column {
        Text(text = "Cronologia ordini per reso", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))

        orders.forEach { order ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = order, modifier = Modifier.padding(8.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    selectedOrder = order
                    showReturnDialog = true
                }) {
                    Text(text = "Richiedi reso")
                }
            }
        }

        if (showReturnDialog) {
            Dialog(onDismissRequest = { showReturnDialog = false }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(Color.White, shape = MaterialTheme.shapes.medium)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            Text(
                                text = "Richiesta reso per $selectedOrder",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Chiudi",
                                modifier = Modifier
                                    .clickable { showReturnDialog = false }
                            )
                        }
                        TextField(
                            value = returnReason,
                            onValueChange = { returnReason = it },
                            label = { Text("Motivo del reso") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { showReturnDialog = false }) {
                            Text(text = "Invia richiesta")
                        }
                    }
                }
            }
        }
    }
}
