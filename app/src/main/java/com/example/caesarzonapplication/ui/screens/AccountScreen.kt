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
import com.example.caesarzonapplication.ui.components.ReportsSection
import com.example.caesarzonapplication.ui.components.ReturnsSection
import com.example.caesarzonapplication.ui.components.UserInfoSection

enum class AccountTab {
    UserInfo,
    PaymentManagement,
    OrderManagement,
    Reports,
    Returns
}

@Composable
fun AccountScreen(padding: PaddingValues) {
    var selectedTab by remember { mutableStateOf(AccountTab.UserInfo) }

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
                AccountTab.values().forEach { tab ->
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
                AccountTab.UserInfo -> UserInfoSection()
                AccountTab.PaymentManagement -> PaymentManagementSection()
                AccountTab.OrderManagement -> OrderManagementSection()
                AccountTab.Reports -> ReportsSection()
                AccountTab.Returns -> ReturnsSection()
            }
        }
    }
}
