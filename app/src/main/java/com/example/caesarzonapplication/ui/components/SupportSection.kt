package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caesarzonapplication.model.dto.SupportDTO
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.SupportRequestsViewModel

@Composable
fun SupportSection(supportViewModel: SupportRequestsViewModel, username: String) {

    var reportText by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedReason by rememberSaveable { mutableStateOf("Seleziona...") }

    val reportReasons = listOf(
        "Ordine",
        "Spedizione",
        "Resi e rimborsi",
        "Domande sui prodotti",
        "Assistenza post-vendita",
        "Problemi tecnici",
        "Modifiche dell'account",
        "Promozione e sconti",
        "Feedback e suggerimenti",
        "Assistenza personalizzata"
    )

    Column {
        Text(text = "Invia richiesta", style = MaterialTheme.typography.bodyMedium)
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = !expanded }
        ) {
            TextField(
                value = selectedReason,
                onValueChange = {},
                label = { Text("Motivo") },
                readOnly = true,
                trailingIcon = {
                    if (expanded)
                        Icons.Filled.ArrowUpward
                    else Icons.Filled.ArrowDropDown
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            DropdownMenu(
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
        TextField(
            value = reportText,
            onValueChange = { reportText = it },
            label = { Text("Descrivi il problema") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )
        Button(onClick = {
            val date = java.time.LocalDateTime.now()
            val support = SupportDTO("", username, selectedReason, "", reportText, date.toString())
            supportViewModel.addSupport(support)}) {
            Text(text = "Invia richiesta")
        }
    }
}
