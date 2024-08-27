package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.R
import com.example.caesarzonapplication.model.dto.UserSearchDTO
import com.example.caesarzonapplication.model.viewmodels.ProductsViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.FollowersViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

enum class UsersTab {
    Utenti,
    Seguiti,
    Amici
}

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun FriendlistScreen(navHostController: NavHostController, followersViewModel: FollowersViewModel) {

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(UsersTab.Utenti) }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            contentAlignment = Alignment.Center,
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
                    .background(Color.White)
                    .padding(top = 30.dp),
                color = Color.Black,
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.padding(8.dp),
                placeholder = { Text(text = "Cerca...") },
                shape = RoundedCornerShape(50),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.LightGray,
                    focusedTextColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Button(
                modifier = Modifier.padding(horizontal = 12.dp),
                onClick = {
                    GlobalScope.launch(Dispatchers.IO) {
                        try {
                            followersViewModel.searchUsers(searchQuery)
                        } catch (e: Exception) {
                            println(e.message)
                        }
                    }
                }
            ) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "search_button")
            }
        }
        ScrollableTabRow(
            selectedTabIndex = selectedTab.ordinal,
            edgePadding = 30.dp,
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
        when (selectedTab) {
            UsersTab.Utenti ->
                if (followersViewModel.users.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        items(followersViewModel.users.filter {
                            it.username.contains(searchQuery, ignoreCase = true)
                        }) { userSearchDTO ->
                            UserRow(userSearchDTO, followersViewModel, navHostController)
                        }
                    }
                } else {
                    Text(
                        modifier = Modifier
                            .padding(top = 150.dp)
                            .padding(horizontal = 80.dp),
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ), text = "Non ci sono utenti"
                    )
                }
            UsersTab.Seguiti ->
                if (followersViewModel.followers.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        items(followersViewModel.followers.filter {
                            it.username.contains(searchQuery, ignoreCase = true)
                        }) { user ->
                            FriendsRow(user, followersViewModel, navHostController)
                        }
                    }
                } else {
                    Text(
                        modifier = Modifier
                            .padding(top = 150.dp)
                            .padding(horizontal = 80.dp),
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ), text = "Non ci sono seguiti"
                    )
                }
            UsersTab.Amici ->
                if (followersViewModel.friends.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        items(followersViewModel.friends.filter {
                            it.username.contains(searchQuery, ignoreCase = true)
                        })
                        { user ->
                            FriendsRow(user, followersViewModel, navHostController)
                        }
                    }
                } else {
                    Text(
                        modifier = Modifier
                            .padding(top = 150.dp)
                            .padding(horizontal = 80.dp),
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        text = "Non ci sono amici"
                    )
                }
        }
    }
}


@Composable
fun UserRow(user: UserSearchDTO, followersAndFriendsViewModel: FollowersViewModel, navHostController: NavHostController) {
    var isFollower by rememberSaveable { mutableStateOf(user.follower) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navHostController.navigate("userpage") },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = user.username,
            modifier = Modifier
                .padding(12.dp)
                .weight(1f)
        )
        if (!isFollower) {
            Image(
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp),
                painter = painterResource(id = R.drawable.logoutente),
                contentDescription = "foto_profilo"
            )
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        followersAndFriendsViewModel.addFollower(user)
                        isFollower = true
                    }
                    .padding(12.dp)
            )
        } else {
            Image(
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp),
                painter = painterResource(id = R.drawable.logoutente),
                contentDescription = "foto_profilo"
            )
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Composable
fun FriendsRow(user: UserSearchDTO, followersAndFriendsViewModel: FollowersViewModel, navHostController: NavHostController) {
    var isFriend by rememberSaveable { mutableStateOf(user.friendStatus) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navHostController.navigate("userpage") },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = user.username,
            modifier = Modifier
                .padding(12.dp)
                .weight(1f)
        )
        Icon(
            imageVector = Icons.Filled.Clear,
            contentDescription = null,
            modifier = Modifier.clickable { followersAndFriendsViewModel.removeFollower(user) }
        )
        if (isFriend) {
            Image(
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp),
                painter = painterResource(id = R.drawable.logoutente),
                contentDescription = "foto_profilo"
            )
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        followersAndFriendsViewModel.toggleFriendStatus(user)
                        isFriend = false
                    }
                    .padding(12.dp)
            )
        } else {
            Image(
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp),
                painter = painterResource(id = R.drawable.logoutente),
                contentDescription = "foto_profilo"
            )
            Icon(
                imageVector = Icons.Filled.FavoriteBorder,
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        followersAndFriendsViewModel.toggleFriendStatus(user)
                        isFriend = true
                    }
                    .padding(12.dp)
            )
        }
    }
}
