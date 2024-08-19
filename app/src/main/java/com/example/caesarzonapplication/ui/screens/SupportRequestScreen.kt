package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.caesarzonapplication.ui.components.SupportRequest
import com.example.caesarzonapplication.model.viewmodels.AdminViewModels.SupportRequestViewModel

@Composable
fun SupportRequestScreen() {

    val supportRequestViewModel = SupportRequestViewModel()

    Column {
        Box(
            Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
        ){
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
        }
        SupportRequest(supportRequestViewModel)
    }
}

