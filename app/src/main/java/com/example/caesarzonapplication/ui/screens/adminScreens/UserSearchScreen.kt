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
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.isAdmin
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.logged
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.myToken
import com.example.caesarzonapplication.model.viewmodels.adminViewModels.SearchAndBanUsersViewModel
import com.example.caesarzonapplication.ui.components.UserBannedComponent
import com.example.caesarzonapplication.navigation.BottomBarScreen
import com.example.caesarzonapplication.ui.components.UserSearchComponent
@Composable
fun UserSearchScreen(navController: NavHostController, searchViewModel: SearchAndBanUsersViewModel) {
    var searchText by rememberSaveable { mutableStateOf("") }
    var showBannedUsers by remember { mutableStateOf(false) }
    val users by searchViewModel.searchResults.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Button(
                modifier = Modifier.padding(horizontal = 30.dp),
                onClick = {
                    isAdmin.value = false
                    logged.value = false
                    myToken?.refreshToken=""
                    myToken?.accessToken=""
                    navController.navigate(BottomBarScreen.Home.route)},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ),

                )
            {
                Text(text = "Logout")
            }

            Text(
                text = "Ricerca utenti",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Cerca utenti") },
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier
                    .background(color = Color.White)
                    .weight(1f)
            )
            IconButton(
                onClick = {
                    searchViewModel.searchUsers(searchText)
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
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                showBannedUsers = !showBannedUsers
                if (showBannedUsers) {
                    searchViewModel.loadBannedUsers()
                }
            })
        {
            Text(text = if (showBannedUsers) "Nascondi utenti bannati" else "Mostra utenti bannati")
        }
        if (showBannedUsers) {
            LazyColumn {
                item {
                    UserBannedComponent(navController, searchViewModel)
                }
            }
        }
        if(users.isNotEmpty()){
            LazyColumn {
                item()
                {
                    UserSearchComponent(navController, searchViewModel)
                }
            }
        }
    }
}

