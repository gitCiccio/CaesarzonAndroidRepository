package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caesarzonapplication.viewmodels.AccountInfoViewModel
import com.example.caesarzonapplication.viewmodels.AdminInfoViewModel

@Composable
fun ReportsSection( adminInfoViewModel: AdminInfoViewModel){

    LazyColumn {
        items(adminInfoViewModel.reports.size){ index ->
            val report = adminInfoViewModel.reports[index]
            var expanded by remember { mutableStateOf(false) }

            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = report.description)
                    Text(text = report.usernameUser2)
                    Text(text = report.reason)
                    Text(text = report.reportDate)
                    Row {
                        IconButton(onClick = { /* Gestisci accetta segnalazione */ }) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = "Accetta")
                                Text(text = "Banna")
                            }
                        }
                        IconButton(onClick = { /* Gestisci scarta segnalazione */ }) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Scarta")
                                Text(text = "Scarta")
                            }
                        }
                        IconButton(onClick = { expanded = !expanded }) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Descrizione")
                                Text(text = "Descrizione")
                            }
                        }
                    }
                }
                if (expanded) {
                    Text(text = "Descrizione dettagliata della segnalazione...")
                }
            }
        }
    }
}

