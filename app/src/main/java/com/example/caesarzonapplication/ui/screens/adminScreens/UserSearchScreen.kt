package com.example.caesarzonapplication.ui.screens.adminScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.model.viewmodels.adminViewModels.SearchAndBanUsersViewModel
import com.example.caesarzonapplication.ui.components.UserSearchComponent
@Composable
fun UserSearchScreen(navController: NavHostController, searchViewModel: SearchAndBanUsersViewModel) {
    var searchText by rememberSaveable { mutableStateOf("") }
    var showBannedUsers by rememberSaveable { mutableStateOf(false) }
    val users by searchViewModel.searchResults.collectAsState()

    LaunchedEffect(Unit) {
        searchViewModel.searchUsers()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header Text
        Text(
            text = "Ricerca utenti",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
            .align(Alignment.CenterHorizontally)
        )

        // Row to contain TextField and Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Search TextField
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Cerca utenti") },
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier
                    .background(color = Color.White)
                    .weight(1f)
            )
            // Search Button
            IconButton(
                onClick = {
                    showBannedUsers = false
                },
                modifier = Modifier.align(Alignment.CenterVertically)
            ){
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        }
        if(users.isNotEmpty()){
            LazyColumn{
                items(users.size){
                    UserSearchComponent(navController, searchViewModel)
                }
            }
        }
    }
}

