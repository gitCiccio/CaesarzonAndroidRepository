package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun PaymentManagementSection() {
    var cardNumber by remember { mutableStateOf(TextFieldValue("")) }
    var cardHolderName by remember { mutableStateOf(TextFieldValue("")) }
    var expirationDate by remember { mutableStateOf(TextFieldValue("")) }
    var cvc by remember { mutableStateOf(TextFieldValue("")) }
    var paymentMethods by remember { mutableStateOf(listOf("**** **** **** 1234", "**** **** **** 5678")) }
    var showAddPaymentDialog by remember { mutableStateOf(false) }
    var showRemovePaymentDialog by remember { mutableStateOf(false) }

    Column {
        Text(text = "Metodi di pagamento", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))

        paymentMethods.forEach { method ->
            Text(text = method, modifier = Modifier.padding(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { showAddPaymentDialog = true }) {
            Text(text = "Aggiungi metodo di pagamento")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { showRemovePaymentDialog = true }) {
            Text(text = "Rimuovi metodo di pagamento")
        }

        if (showAddPaymentDialog) {
            Dialog(onDismissRequest = { showAddPaymentDialog = false }) {
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
                                text = "Aggiungi Metodo di Pagamento",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Chiudi",
                                modifier = Modifier
                                    .clickable { showAddPaymentDialog = false }
                            )
                        }
                        TextField(
                            value = cardHolderName,
                            onValueChange = { cardHolderName = it },
                            label = { Text("Nome e Cognome del Titolare") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = cardNumber,
                            onValueChange = { cardNumber = it },
                            label = { Text("Numero Carta di Credito") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = expirationDate,
                            onValueChange = { expirationDate = it },
                            label = { Text("Data di Scadenza (MM/AA)") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = cvc,
                            onValueChange = { cvc = it },
                            label = { Text("CVC") }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            paymentMethods = paymentMethods + "${cardNumber.text} (Scadenza: ${expirationDate.text})"
                            showAddPaymentDialog = false
                        }) {
                            Text(text = "Salva")
                        }
                    }
                }
            }
        }

        if (showRemovePaymentDialog) {
            Dialog(onDismissRequest = { showRemovePaymentDialog = false }) {
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
                                text = "Rimuovi Metodo di Pagamento",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Chiudi",
                                modifier = Modifier
                                    .clickable { showRemovePaymentDialog = false }
                            )
                        }
                        paymentMethods.forEach { method ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = method, modifier = Modifier.weight(1f))
                                IconButton(onClick = {
                                    paymentMethods = paymentMethods - method
                                }) {
                                    Icon(
                                        Icons.Filled.Close,
                                        contentDescription = "Rimuovi"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
