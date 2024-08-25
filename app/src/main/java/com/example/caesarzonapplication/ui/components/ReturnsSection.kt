package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ReturnsSection() {
    val orders = listOf("Ordine #1", "Ordine #2", "Ordine #3")
    var showReturnDialog by rememberSaveable { mutableStateOf(false) }
    var selectedOrder by rememberSaveable { mutableStateOf("") }
    var returnReason by rememberSaveable { mutableStateOf(TextFieldValue("")) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Cronologia ordini per reso",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 16.dp)
        )

        orders.forEach { order ->
            OrderRow(order = order, onReturnClick = {
                selectedOrder = order
                showReturnDialog = true
            })
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider( thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp),color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
        }

        if (showReturnDialog) {
            ReturnRequestDialog(
                selectedOrder = selectedOrder,
                returnReason = returnReason,
                onReasonChange = { returnReason = it },
                onDismiss = { showReturnDialog = false },
                onSubmit = {
                    showReturnDialog = false
                    // returnViewModel.submitReturnRequest(selectedOrder, returnReason.text)
                }
            )
        }
    }
}

@Composable
fun OrderRow(order: String, onReturnClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = order,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Button(onClick = onReturnClick) {
            Text(text = "Richiedi reso")
        }
    }
}

@Composable
fun ReturnRequestDialog(
    selectedOrder: String,
    returnReason: TextFieldValue,
    onReasonChange: (TextFieldValue) -> Unit,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
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
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Chiudi",
                        modifier = Modifier.clickable { onDismiss() }
                    )
                }
                TextField(
                    value = returnReason,
                    onValueChange = onReasonChange,
                    label = { Text("Motivo del reso") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onSubmit, modifier = Modifier.align(Alignment.End)) {
                    Text(text = "Invia richiesta")
                }
            }
        }
    }
}
