package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caesarzonapplication.model.viewmodels.AdminViewModels.SearchAndBanUsersViewModel

@Composable
fun UserSearchComponent(searchAndBanUsersViewModel: SearchAndBanUsersViewModel) {

    if (searchAndBanUsersViewModel.searchResults.isEmpty()) {
        Text(
            modifier = Modifier
                    .padding(top = 150.dp)
                    .padding(horizontal = 80.dp),
            text = "Nessun utente trovato",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
    }
    else{
        searchAndBanUsersViewModel.searchResults.forEach { user ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = user.username)
                Button(
                    onClick = { searchAndBanUsersViewModel.banUser(user) }
                ) {
                    Text(text = "Banna utente")
                }
            }
        }
    }
}
