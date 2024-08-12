package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.navigation.compose.rememberNavController
import com.example.caesarzonapplication.ui.components.AdminNavigationBottomBar
import com.example.caesarzonapplication.ui.components.BanSection
import com.example.caesarzonapplication.ui.components.UserSearchComponent
import com.example.caesarzonapplication.model.viewmodels.AdminViewModels.SearchAndBanUsersViewModel

@Composable
fun UserSearchScreen(SearchAndBanViewModel: SearchAndBanUsersViewModel) {
    val navController = rememberNavController()
    var searchText by rememberSaveable { mutableStateOf("") }
    var showBannedUsers by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column {
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
                Spacer(modifier = Modifier.padding(top = 10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(35.dp))
                    TextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        label = { Text("Cerca utenti") },
                        modifier = Modifier.width(320.dp)
                    )
                    IconButton(onClick = { SearchAndBanViewModel.searchSpecifcUsers(searchText) }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    }
                }
                Spacer(modifier = Modifier.padding(top = 10.dp))
                Row{
                   Button(modifier = Modifier.padding(horizontal = 105.dp), onClick = { showBannedUsers = !showBannedUsers }) {
                       if (!showBannedUsers) {
                           Text(text = "Mostra tutti gli utenti")
                       }else{
                           Text(text = "Mostra utenti bannati")
                       }
                   }
                }
            }
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                if(!showBannedUsers)
                    UserSearchComponent(searchAndBanUsersViewModel = SearchAndBanViewModel)
                else
                    BanSection(banViewModel = SearchAndBanViewModel)
            }
        },
        bottomBar = { Spacer(modifier = Modifier.padding(60.dp))}
    )
}

