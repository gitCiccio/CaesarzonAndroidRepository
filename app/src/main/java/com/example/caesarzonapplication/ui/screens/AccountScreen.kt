package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.caesarzonapplication.R
import com.example.caesarzonapplication.ui.components.OrderManagementSection
import com.example.caesarzonapplication.ui.components.PaymentManagementSection
import com.example.caesarzonapplication.ui.components.SupportSection
import com.example.caesarzonapplication.ui.components.ReturnsSection
import com.example.caesarzonapplication.ui.components.UserInfoSection
import com.example.caesarzonapplication.viewmodels.UserViewModel

enum class AccountTab {
    Profilo,
    Pagamenti,
    Ordini,
    Assistenza,
    Resi
}

@Composable
fun AccountScreen(padding: PaddingValues, userViewModel: UserViewModel) {
    var selectedTab by remember { mutableStateOf(AccountTab.Profilo) }

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
            Image(
                painter = painterResource(id = R.drawable.logoutente),
                contentDescription = "User Profile",
                modifier = Modifier
                    .size(100.dp)
                    .padding(16.dp)
            )

            Text(text = "Profilo Utente", style = MaterialTheme.typography.headlineLarge)

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
                AccountTab.entries.forEach { tab ->
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
                AccountTab.Profilo -> UserInfoSection(userViewModel)
                AccountTab.Pagamenti -> PaymentManagementSection()
                AccountTab.Ordini -> OrderManagementSection()
                AccountTab.Assistenza -> SupportSection()
                AccountTab.Resi -> ReturnsSection()
            }
        }
    }
}
