package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caesarzonapplication.viewmodels.AdminViewModels.SearchUsersViewModel
import com.example.caesarzonapplication.viewmodels.BanViewModel

@Composable
fun UserSearchComponent(searchUsersViewModel: SearchUsersViewModel,banViewModel: BanViewModel) {
    Spacer(modifier = Modifier.height(30.dp))

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(searchUsersViewModel.searchResults) { user ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = user.username)
                Row {
                    Button(onClick = { banViewModel.banUser(user) }) {
                        Text(text = "Banna utente")
                    }
                }
            }
        }
    }
}
