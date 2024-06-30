package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportSection() {
    var reportText by rememberSaveable { mutableStateOf(TextFieldValue("")) }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedReason by rememberSaveable { mutableStateOf("Seleziona...") }
    val reportReasons = listOf("Ordine", "Spedizione", "Resi e rimborsi", "Domande sui prodotti", "Assistenza post-vendita", "Problemi tecnici", "Modifiche dell'account", "Promozione e sconti", "Feedback e suggerimenti", "Assistenza personalizzata")

    Column {
        Text(text = "Invia richiesta", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedReason,
                onValueChange = {},
                label = { Text("Motivo") },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                reportReasons.forEach { reason ->
                    DropdownMenuItem(
                        text = { Text(text = reason) },
                        onClick = {
                            selectedReason = reason
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = reportText,
            onValueChange = { reportText = it },
            label = { Text("Descrivi il problema") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { /* Logica per inviare la richiesta */ }) {
            Text(text = "Invia richiesta")
        }
    }
}
