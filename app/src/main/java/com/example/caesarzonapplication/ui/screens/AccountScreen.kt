package com.example.caesarzonapplication.ui.screens

import android.graphics.ImageDecoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caesarzonapplication.R
import com.example.caesarzonapplication.ui.components.OrderManagementSection
import com.example.caesarzonapplication.ui.components.PaymentManagementSection
import com.example.caesarzonapplication.ui.components.SupportSection
import com.example.caesarzonapplication.ui.components.ReturnsSection
import com.example.caesarzonapplication.ui.components.UserAddressInfoSection
import com.example.caesarzonapplication.ui.components.UserInfoSection
import com.example.caesarzonapplication.viewmodels.AccountInfoViewModel

enum class AccountTab {
    Profilo,
    Indirizzi,
    Carte,
    Ordini,
    Resi,
    Assistenza,
}

@Composable
fun AccountScreen(padding: PaddingValues, accountInfoViewModel: AccountInfoViewModel) {
    var selectedTab by remember { mutableStateOf(AccountTab.Profilo) }
    val context = LocalContext.current

    // State for holding the loaded profile image
    val profileImage by accountInfoViewModel.profileImage.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                val bitmap = ImageDecoder.decodeBitmap(source)
                accountInfoViewModel.setProfileImage(bitmap)
            }
        }
    )

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
            if (profileImage != null) {
                Image(
                    bitmap = profileImage!!.asImageBitmap(),
                    contentDescription = "User Profile",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(16.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.logoutente),
                    contentDescription = "User Profile",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(16.dp)
                )
            }

            Button(onClick = { launcher.launch("image/*") }) {
                Text("Carica Immagine")
            }

            when(selectedTab){
                AccountTab.Profilo -> {
                    Text(text = "Il tuo profilo", fontSize = 30.sp)
                }
                AccountTab.Carte -> {
                    Text(text = "Le tue Carte", fontSize = 30.sp)
                }
                AccountTab.Ordini -> {
                    Text(text = "I tuoi ordini", fontSize = 30.sp)
                }
                AccountTab.Resi -> {
                    Text(text = "I tuoi resi", fontSize = 30.sp)
                }
                AccountTab.Assistenza -> {
                    Text(text = "Come possiamo aiutarti?", fontSize = 30.sp)
                }

                AccountTab.Indirizzi -> TODO()
            }

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
                        onClick = { selectedTab = tab }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTab) {
                AccountTab.Profilo -> UserInfoSection()
                AccountTab.Indirizzi -> UserAddressInfoSection(accountInfoViewModel)
                AccountTab.Carte -> PaymentManagementSection()
                AccountTab.Ordini -> OrderManagementSection()
                AccountTab.Resi -> ReturnsSection()
                AccountTab.Assistenza -> SupportSection()
            }
        }
    }

    // Load profile image from database when AccountScreen is first opened
    LaunchedEffect(true) {
        accountInfoViewModel.loadProfileImageFromDatabase()
    }
}