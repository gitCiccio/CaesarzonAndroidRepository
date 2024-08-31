package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.caesarzonapplication.R
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.globalUsername
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.WishlistViewModel
import com.example.caesarzonapplication.ui.screens.WishlistTab
enum class WishlistTabs {
    Pubbliche,
    Condivise
}

@Composable
fun WishListComponent(wishlistViewModel: WishlistViewModel, vis: Int, username: String){
    var selectedTab by remember { mutableStateOf(WishlistTabs.Pubbliche) }

    LaunchedEffect(Unit){
        wishlistViewModel.getUserWishlists(username, vis)

    }

    Spacer(modifier = Modifier.height(8.dp))
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        item {
            Spacer(modifier = Modifier.height(50.dp))
            Text( modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
                text = "Liste dei desideri di ${username}",
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
                    val tabs = WishlistTabs.entries.toTypedArray()
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
                WishlistTabs.Pubbliche -> WishlistSection(0, wishlistViewModel)
                WishlistTabs.Condivise -> WishlistSection(1, wishlistViewModel)
            }
        }
        item { Spacer(modifier = Modifier.height(20.dp)) }
    }

}