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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caesarzonapplication.model.viewmodels.AdminViewModels.SearchAndBanUsersViewModel

@Composable
fun BanSection(banViewModel: SearchAndBanUsersViewModel) {

    LazyColumn {
        items(banViewModel.bans.size) { index ->
            val ban = banViewModel.bans[index]
            Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = ban.endDate)
                    Text(text = ban.userUsername)
                    Text(text = ban.reason)
                    Row {
                        Button(onClick = { banViewModel.deleteBan(ban)}){
                            Text(text = "Rimuovi ban")
                        }
                    }
                }
            }
        }
    }
}