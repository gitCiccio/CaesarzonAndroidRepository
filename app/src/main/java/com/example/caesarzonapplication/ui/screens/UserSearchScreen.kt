package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caesarzonapplication.model.viewmodels.AdminViewModels.SearchAndBanUsersViewModel
import com.example.caesarzonapplication.ui.components.BanSection
import com.example.caesarzonapplication.ui.components.UserSearchComponent

@Composable
fun UserSearchScreen(searchAndBanViewModel: SearchAndBanUsersViewModel) {

    var searchText by rememberSaveable { mutableStateOf("") }
    var showBannedUsers by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = "Ricerca utenti",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                color = Color.Black
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Cerca utenti") },
                modifier = Modifier.width(320.dp)
            )
            IconButton(
                onClick = { searchAndBanViewModel.searchUsers() }
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
        }
        Button(
            modifier = Modifier.padding(end = 8.dp),
            onClick = { showBannedUsers = !showBannedUsers }
        ) {
            if (!showBannedUsers) {
               Text(text = "Mostra tutti gli utenti")
            }else{
               Text(text = "Mostra utenti bannati")
            }
        }
    }
    LazyColumn(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = {
            item {
                if(!showBannedUsers)
                    UserSearchComponent(searchAndBanViewModel)
                else BanSection(searchAndBanViewModel)
            }
        }
    )
}


