package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddReaction
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.model.dto.notificationDTO.BanDTO
import com.example.caesarzonapplication.model.dto.userDTOS.UserFindDTO
import com.example.caesarzonapplication.model.viewmodels.adminViewModels.SearchAndBanUsersViewModel

@Composable
fun UserBannedComponent(
    navController: NavHostController,
    searchAndBanUsersViewModel: SearchAndBanUsersViewModel
) {
    val bannedUsers by searchAndBanUsersViewModel.bannedUsers.collectAsState()

    var selectedBannedUser by remember { mutableStateOf<BanDTO?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        if (bannedUsers.isEmpty()) {
            Text(
                modifier = Modifier
                    .padding(top = 150.dp)
                    .padding(horizontal = 80.dp),
                text = "Nessun utente bannato",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
        } else {
            bannedUsers.forEach { bannedUser ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            selectedBannedUser =
                                if (selectedBannedUser == bannedUser) null else bannedUser
                        },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Image",
                        modifier = Modifier.size(50.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = bannedUser.userUsername,
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .weight(1f),
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                }

                if (selectedBannedUser == bannedUser) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Data inizio ban: ${bannedUser.startDate}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Ban ancora attivo",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        IconButton(
                            onClick = {
                                searchAndBanUsersViewModel.deleteBan(bannedUser)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddReaction,
                                contentDescription = "Sbanna Utente"
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(30.dp))
            }
        }
    }
}