package com.example.caesarzonapplication.ui.screens

import UserSearchSection
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.ui.components.*
import com.example.caesarzonapplication.viewmodels.AdminInfoViewModel

enum class AdminTab {
    RicercaUtenti,
    Segnalazioni,
    RichiesteSupporto,
    Ban
}

@Composable
fun AdminScreen(padding: PaddingValues, adminInfoViewModel: AdminInfoViewModel) {
    var selectedTab by remember { mutableStateOf(AdminTab.RicercaUtenti) }
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
            Text(text = "Admin Area", style = MaterialTheme.typography.headlineLarge)

            Spacer(modifier = Modifier.height(50.dp))

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
                AdminTab.entries.forEach { tab ->
                    Tab(
                        text = { Text(text = tab.name) },
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTab) {
                AdminTab.RicercaUtenti -> UserSearchSection(adminInfoViewModel)
                AdminTab.Segnalazioni -> ReportsSection(adminInfoViewModel)
                AdminTab.RichiesteSupporto -> SupportRequestSection(adminInfoViewModel)
                AdminTab.Ban -> BanSection(adminInfoViewModel)
            }
        }
    }
}
