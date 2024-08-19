package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caesarzonapplication.model.viewmodels.WishlistViewModel

enum class WishlistTab {
    Pubbliche,
    Private,
    Condivise
}

@Composable
fun WishlistPopup(wishlistViewModel: WishlistViewModel, onDismiss: () -> Unit) {
    var selectedTab by remember { mutableStateOf(WishlistTab.Pubbliche) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = "Seleziona la lista desideri",
                style = TextStyle(fontSize = 18.sp, textAlign = TextAlign.Center)
            )
        },
        text = {
            Column(modifier = Modifier.height(350.dp)) {
                ScrollableTabRow(
                    modifier = Modifier.height(48.dp),
                    selectedTabIndex = selectedTab.ordinal,
                    edgePadding = 0.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier
                                .tabIndicatorOffset(tabPositions[selectedTab.ordinal])
                                .height(4.dp),
                            color = Color.Black
                        )
                    }
                ) {
                    val tabs = WishlistTab.values()
                    tabs.forEachIndexed { index, tab ->
                        Tab(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            text = { Text(tab.name, style = TextStyle(fontSize = 14.sp)) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                WishlistViewForProduct(
                    wishlistViewModel, visibility = when (selectedTab) {
                        WishlistTab.Pubbliche -> 0
                        WishlistTab.Private -> 2
                        WishlistTab.Condivise -> 1
                    }
                )
            }
               },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text("Annulla", style = TextStyle(color = Color.Black, fontSize = 16.sp))
            }
        }
    )
}
