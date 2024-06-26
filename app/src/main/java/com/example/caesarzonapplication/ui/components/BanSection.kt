package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caesarzonapplication.viewmodels.AccountInfoViewModel
import com.example.caesarzonapplication.viewmodels.AdminInfoViewModel

/*@Composable
fun BanSection( adminInfoViewModel: AdminInfoViewModel) {
    //val bans by adminInfoViewModel.bans

    LazyColumn {
        items(bans.size) { index ->
            val ban = bans[index]
            var expanded by remember { mutableStateOf(false) }
            Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = ban.date)
                    Text(text = ban.bannedUser)
                    Text(text = ban.reason)
                    Row {
                        IconButton(onClick = { /* Gestisci rimuovi ban */ }) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Rimuovi")
                                Text(text = "Rimuovi")
                            }
                        }
                        IconButton(onClick = { expanded = !expanded }) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Espandi")
                                Text(text = "Descrizione")
                            }
                        }
                    }
                }
                if (expanded) {
                    Text(text = "Descrizione dettagliata del ban...")
                }
            }
        }
    }
}*/