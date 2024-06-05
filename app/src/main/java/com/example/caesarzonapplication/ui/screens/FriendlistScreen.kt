package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.caesarzonapplication.model.User
import com.example.caesarzonapplication.ui.components.NavigationBottomBar
import com.example.caesarzonapplication.viewmodels.UserViewModel

@Composable
fun FriendlistScreen(userViewModel: UserViewModel= viewModel()) {
    var searchQuery by remember {
        mutableStateOf("")
    }

    Scaffold (
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(40.dp),
                contentAlignment = Alignment.Center
            )
            {
                Text(
                    text = "La tua friendlist",
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .background(Color.White),
                    color = Color.Black,
                )
            }
        },
        bottomBar = { NavigationBottomBar(navController = rememberNavController())},
        content = {padding ->
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.White)
            ){
                LazyColumn (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ){
                    items(userViewModel.users.filter {
                        it.username.contains(searchQuery, ignoreCase = true)
                    }){
                        user ->
                        UserRow(remember { user }, userViewModel)
                    }
                }
            }
        }
    )
}

@Composable
fun UserRow(user: User, userViewModel: UserViewModel) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(text= user.username)
        IconButton(onClick = {userViewModel.toggleFavorite(user)}) {
            if(user.isFavorite){
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null
                )
            }else{
                Icon(
                    imageVector = Icons.Filled.FavoriteBorder,
                    contentDescription = null)
            }
        }
    }
}
