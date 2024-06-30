package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caesarzonapplication.model.dto.ReportDTO
import com.example.caesarzonapplication.viewmodels.AdminViewModels.ReportViewModel

@Composable
fun ReportCardTab(report : ReportDTO, reportViewModel: ReportViewModel) {

    var expanded by rememberSaveable { mutableStateOf(false) }
    var popupMessage by rememberSaveable { mutableStateOf("") }
    var showPopup by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Data segnalazione: ${report.reportDate}")
                    Text(text = "Motivazione:")
                    Text(text = report.reason)
                    Text(text = "Utente segnalato:")
                    Text(text = report.usernameUser2)
                }
                Row(modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.End
                ){
                    IconButton(
                        onClick = {
                            reportViewModel.deleteReport(report, true)
                            popupMessage = "Segnalazione accettata con successo!"
                            showPopup = true
                                  },
                        modifier = Modifier.size(60.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Accetta"
                            )
                            Text(text = "Accetta", fontSize = 12.sp)
                        }
                    }
                    IconButton(
                        onClick = {
                            reportViewModel.deleteReport(report, false)
                            popupMessage = "Segnalazione scartata con successo!"
                            showPopup = true
                                  },
                        modifier = Modifier.size(60.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Scarta"
                            )
                            Text(text = "Scarta", fontSize = 12.sp)
                        }
                    }
                }
            }
            IconButton(onClick = { expanded = !expanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Descrizione"
                    )
                    Text(text = "Descrizione", fontSize = 14.sp)
                }
            }
            if(expanded){
                Text(text = report.description,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
        }
    }

    if (showPopup) {
        GenericMessagePopup(message = popupMessage) {
            showPopup = false
        }
    }
}