package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.caesarzonapplication.ui.components.UserSearchComponent
import com.example.caesarzonapplication.viewmodels.AdminViewModels.SearchUsersViewModel
import com.example.caesarzonapplication.viewmodels.BanViewModel

@Composable
fun UserSearchScreen(adminInfoViewModel: SearchUsersViewModel) {
    val navController = rememberNavController()
    var searchText by remember { mutableStateOf("") }

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
                    IconButton(onClick = { adminInfoViewModel.searchSpecifcUsers(searchText) }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    }
                }
            }
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                UserSearchComponent(searchUsersViewModel = adminInfoViewModel, banViewModel = BanViewModel())
            }
        },
        bottomBar = {
            AdminNavigationBottomBar(navController = navController)
        }
    )
}

