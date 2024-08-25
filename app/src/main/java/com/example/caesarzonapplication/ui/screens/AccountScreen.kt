package com.example.caesarzonapplication.ui.screens

import android.graphics.ImageDecoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.caesarzonapplication.ui.components.OrderManagementSection
import com.example.caesarzonapplication.ui.components.PaymentManagementSection
import com.example.caesarzonapplication.ui.components.ReturnsSection
import com.example.caesarzonapplication.ui.components.SupportSection
import com.example.caesarzonapplication.ui.components.UserAddressInfoSection
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AccountInfoViewModel
import com.example.caesarzonapplication.navigation.AccountTabRow
import com.example.caesarzonapplication.ui.components.UserInfoSection

@Composable
fun AccountScreen(navController: NavController, accountInfoViewModel: AccountInfoViewModel) {

    val accountTabs = listOf(
        AccountTabRow.Profile,
        AccountTabRow.Addresses,
        AccountTabRow.Cards,
        AccountTabRow.Orders,
        AccountTabRow.Returns,
        AccountTabRow.Support
    )

    var selectedTab by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                val bitmap = ImageDecoder.decodeBitmap(source)
                //accountInfoViewModel.setProfileImage(bitmap)
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            accountInfoViewModel.profileImage.let {
                accountInfoViewModel.profileImage.value?.profilePicture?.asImageBitmap()?.let { it1 ->
                    Image(
                        bitmap = it1,
                        contentDescription = "User Profile",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(16.dp)
                    )
                }
            }
            Button(onClick = { launcher.launch("image/*") }) {
                Text("Carica Immagine")
            }
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                edgePadding = (0.dp),
                containerColor = TabRowDefaults.secondaryContainerColor,
                ) {
                Spacer(modifier = Modifier.height(16.dp))
                accountTabs.forEachIndexed { index, tab ->
                    Tab(
                        text = { Text(text = tab.name) },
                        selected = index == selectedTab,
                        onClick = { selectedTab = index },
                        icon = {
                            Icon(
                                imageVector = if(index == selectedTab) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.name
                            )
                        },
                        unselectedContentColor = TabRowDefaults.secondaryContentColor,
                        selectedContentColor = TabRowDefaults.primaryContentColor,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
            when (selectedTab) {
                0 -> UserInfoSection(accountInfoViewModel)
                1 -> UserAddressInfoSection(accountInfoViewModel)
                2 -> PaymentManagementSection()
                3 -> OrderManagementSection()
                4 -> ReturnsSection()
                5 -> SupportSection()
            }
        }
    }
}