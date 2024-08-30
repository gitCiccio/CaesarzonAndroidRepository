package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CheckoutScreen() {
    var addresses by remember { mutableStateOf(listOf<String>()) }
    var cards by remember { mutableStateOf(listOf<String>()) }
    val orderTotal = 100.0

    var selectedAddress by remember { mutableStateOf<String?>(null) }
    var selectedCard by remember { mutableStateOf<String?>(null) }
    var payPal by remember { mutableStateOf(false) }

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(text = "Seleziona un indirizzo per la spedizione", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            if (addresses.isEmpty()) {
                Button(
                    onClick = { /* Logica per aggiungere un indirizzo */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors()
                ) {
                    Text(text = "Aggiungi Indirizzo")
                }
            } else {
                addresses.forEach { address ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                    ) {
                        RadioButton(
                            selected = selectedAddress == address && !payPal,
                            onClick = {
                                selectedAddress = address
                                selectedCard = null
                                payPal = false
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = address)
                    }
                }
            }
        }

        item {
            Text(text = "Seleziona un metodo di pagamento", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            if (cards.isEmpty()) {
                Button(
                    onClick = { /* Logica per aggiungere una carta */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors()
                ) {
                    Text(text = "Aggiungi Carta")
                }
            } else {
                cards.forEach { card ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedCard == card && !payPal,
                            onClick = {
                                selectedCard = card
                                selectedAddress = null
                                payPal = false
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = card)
                    }
                }
            }
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Text(
                    text = "Oppure paga con PayPal",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                RadioButton(
                    selected = payPal,
                    onClick = {
                        payPal = true
                        selectedCard = null
                        selectedAddress = null
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Text(text = "Riepilogo Ordine", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Totale: €${"%.2f".format(orderTotal)}", style = MaterialTheme.typography.bodyLarge)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { /* Torna al carrello */ },
                    colors = ButtonDefaults.buttonColors(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Torna al carrello", color = Color.White)
                }

                Button(
                    onClick = {
                        if (selectedAddress != null || selectedCard != null || payPal) {
                            // Procedi all'acquisto con l'opzione selezionata
                        } else {
                            // Mostra un messaggio di errore o avviso
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(),
                    enabled = selectedAddress != null && (selectedCard != null || payPal)                ) {
                    Text(text = "Procedi all'acquisto", color = Color.White)
                }
            }
        }
    }
}
