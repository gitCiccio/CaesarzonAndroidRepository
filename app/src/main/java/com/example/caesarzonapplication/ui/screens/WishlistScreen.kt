package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.NotificationViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.WishlistViewModel
import com.example.caesarzonapplication.ui.components.WishlistSection


enum class WishlistTab {
    Pubbliche,
    Private,
    Condivise
}

@Composable
fun WishlistScreen(navController : NavHostController, notificationViewModel: NotificationViewModel, wishlistViewModel: WishlistViewModel) {

    var selectedTab by remember { mutableStateOf(WishlistTab.Pubbliche) }

    LaunchedEffect(Unit) {
        notificationViewModel.getNotification()
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        item {
            Spacer(modifier = Modifier.height(50.dp))
            Text( modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
                text = "Le tue liste desideri",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            ScrollableTabRow(
                modifier = Modifier.height(80.dp),
                selectedTabIndex = selectedTab.ordinal,
                edgePadding = 0.dp,
                indicator = { TabRowDefaults.PrimaryIndicator(Modifier, height = 10.dp, color = Color.Transparent)},
                tabs ={
                    val tabs = WishlistTab.entries.toTypedArray()
                    tabs.forEachIndexed { _, tab ->
                    Tab(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                        ,
                        text = { Text(tab.name) },
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab })
                }}
            )
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
            when (selectedTab) {
                WishlistTab.Pubbliche -> WishlistSection(0, wishlistViewModel)
                WishlistTab.Private -> WishlistSection(2, wishlistViewModel)
                WishlistTab.Condivise -> WishlistSection(1, wishlistViewModel)
            }
        }
        item { Spacer(modifier = Modifier.height(20.dp)) }
    }
}
