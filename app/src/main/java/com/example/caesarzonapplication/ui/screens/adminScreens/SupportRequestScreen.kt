package com.example.caesarzonapplication.ui.screens.adminScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caesarzonapplication.model.viewmodels.adminViewModels.AdminSupportRequestViewModel
import com.example.caesarzonapplication.ui.components.SupportUser


@Composable
fun SupportRequestScreen(adminSupportRequestViewModel: AdminSupportRequestViewModel) {

    val supportRequests by adminSupportRequestViewModel.supportRequests.collectAsState()

    LaunchedEffect(Unit) {
        adminSupportRequestViewModel.searchSupportRequests()
    }

    Column {
        Text(
            text = "Assistenza",
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(top = 30.dp),
            color = Color.Black,
        )
        if(supportRequests.isNotEmpty()){
            LazyColumn(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                items(supportRequests.size) { support ->
                    SupportUser(supportRequests[support], adminSupportRequestViewModel)
                }
            }
        }else{
            Text(
                modifier = Modifier
                    .padding(top = 150.dp)
                    .padding(horizontal = 80.dp),
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                text = "Non ci sono richieste di assistenza"
            )
        }
    }
}

