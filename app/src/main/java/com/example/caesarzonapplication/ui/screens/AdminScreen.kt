package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caesarzonapplication.ui.components.BanSection
import com.example.caesarzonapplication.ui.components.UserInfoSection
import com.example.caesarzonapplication.viewmodels.AdminInfoViewModel

enum class AdminTab{
    Informazioni,
    Ban
}


@Composable

fun AdminScreen(padding: PaddingValues, adminInfoViewModel: AdminInfoViewModel) {
    var selectedTab by remember { mutableStateOf(AdminTab.Informazioni) }

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
                AdminTab.Informazioni -> UserInfoSection()
                AdminTab.Ban -> BanSection(adminInfoViewModel)
            }
        }
    }
}
