package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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


enum class UsersTab {
    Utenti,
    Seguiti,
    Amici
}
@Composable
fun FriendlistScreen(userViewModel: UserViewModel= viewModel()) {
    var searchQuery by remember {
        mutableStateOf("")
    }
    var selectedTab by remember { mutableStateOf(UsersTab.Utenti) }

    Scaffold(
        topBar = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "La tua friendlist",
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White),
                        color = Color.Black,
                    )
                }
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text(text = "Cerca...") },
                    shape = RoundedCornerShape(50),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.LightGray,
                        focusedTextColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }
        },
        bottomBar = {
            val logged = false
            NavigationBottomBar(navController = rememberNavController(), logged = logged)
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.White)
            ) {
                ScrollableTabRow(
                    selectedTabIndex = selectedTab.ordinal,
                    edgePadding = 0.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal])
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    UsersTab.entries.forEach { tab ->
                        Tab(
                            text = { Text(text = tab.name) },
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (selectedTab) {
                    UsersTab.Utenti ->
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        ) {
                            items(userViewModel.users.filter {
                                it.username.contains(searchQuery, ignoreCase = true)
                            }) { user ->
                                UserRow( user, userViewModel)
                            }
                        }
                    UsersTab.Seguiti ->
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        ) {
                            items(userViewModel.followers.filter {
                                it.username.contains(searchQuery, ignoreCase = true)
                            }) { user ->
                                FriendsRow(user, userViewModel)
                            }
                        }
                    UsersTab.Amici ->
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        ) {
                            items(userViewModel.friends.filter {
                                it.username.contains(searchQuery, ignoreCase = true)
                            }) { user ->
                                FriendsRow( user, userViewModel)
                            }
                        }
                }
            }
        }
    )
}

@Composable
fun UserRow(user: User, userViewModel: UserViewModel) {
    var isFollower by remember { mutableStateOf(user.isFollower) }
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = user.username,
            modifier = Modifier
                .padding(12.dp)
                .weight(1f)
        )
        if (!isFollower) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                modifier = Modifier
                    .clickable { userViewModel.addFollower(user); isFollower = true }
                    .padding(12.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                modifier = Modifier
                    .clickable { userViewModel.removeFollower(user); isFollower = false }
                    .padding(12.dp)
            )
        }
    }
}

@Composable
fun FriendsRow(user: User, userViewModel: UserViewModel) {
    var isFriend by remember { mutableStateOf(user.isFriend) }
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = user.username,
            modifier = Modifier
                .padding(12.dp)
                .weight(1f)
        )
        Icon(
            imageVector = Icons.Filled.Clear,
            contentDescription = null,
            modifier = Modifier
                .clickable { userViewModel.removeFollower(user) }
        )
        if (isFriend) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                modifier = Modifier
                    .clickable { userViewModel.toggleFriendStatus(user); isFriend = false }
                    .padding(12.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Filled.FavoriteBorder,
                contentDescription = null,
                modifier = Modifier
                    .clickable { userViewModel.toggleFriendStatus(user); isFriend = true  }
                    .padding(12.dp)
            )
        }
    }
}



