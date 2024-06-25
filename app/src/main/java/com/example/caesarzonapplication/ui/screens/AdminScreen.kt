package com.example.caesarzonapplication.ui.screens

import UserSearchSection
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caesarzonapplication.ui.components.*
import com.example.caesarzonapplication.viewmodels.AccountInfoViewModel

enum class AdminTab {
    RICERCA_UTENTI,
    SEGNALAZIONI,
    RICHIESTA_SUPPORTO,
    BAN
}

@Composable
fun AdminScreen(padding: PaddingValues, accountInfoViewModel: AccountInfoViewModel) {
    var selectedTab by remember { mutableStateOf(AdminTab.RICERCA_UTENTI) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Admin Profilo", style = MaterialTheme.typography.headlineLarge)

            Spacer(modifier = Modifier.height(16.dp))

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
                AdminTab.values().forEach { tab ->
                    Tab(
                        text = { Text(text = tab.name) },
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTab) {
                AdminTab.RICERCA_UTENTI -> UserSearchSection(accountInfoViewModel)
                AdminTab.SEGNALAZIONI -> ReportsSection(accountInfoViewModel)
                AdminTab.RICHIESTA_SUPPORTO -> SupportRequestSection(accountInfoViewModel)
                AdminTab.BAN -> BanSection(accountInfoViewModel)
            }
        }
    }
}
