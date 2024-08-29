package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.caesarzonapplication.model.dto.SupportDTO
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.SupportRequestsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportSection(supportViewModel: SupportRequestsViewModel, username: String) {

    var reportText by remember { mutableStateOf(TextFieldValue("")) }
    var subjectText by remember { mutableStateOf(TextFieldValue("")) }
    var expanded by remember { mutableStateOf(false) }
    var selectedReason by remember { mutableStateOf("Seleziona...") }

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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Richiesta di supporto",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 10.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp
        )
        ExposedDropdownMenuBox(
            onExpandedChange = { expanded = !expanded },
            expanded = expanded
        ) {
            TextField(
                value = selectedReason,
                onValueChange = {},
                label = { Text("Motivo") },
                readOnly = true,
                trailingIcon = {
                    IconButton(
                        onClick = { expanded = !expanded }
                    ) {
                        Icon(
                            imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .menuAnchor()
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
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
            value = subjectText,
            onValueChange = { subjectText = it },
            label = { Text("Titolo richiesta") },
            modifier = Modifier
                .fillMaxWidth()
        )
        TextField(
            value = reportText,
            onValueChange = { reportText = it },
            label = { Text("Descrivi il problema") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )
        Button(
            onClick = {
                val date = java.time.LocalDateTime.now()
                val support = SupportDTO("", username,  selectedReason, subjectText.toString(), reportText.toString(), date.toString())
                supportViewModel.addSupport(support)
            }
        ) {
            Text(text = "Invia richiesta")
        }
    }
}
