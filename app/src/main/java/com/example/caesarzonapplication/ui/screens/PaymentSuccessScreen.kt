package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.NotificationViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.ShoppingCartViewModel
import kotlinx.coroutines.delay

@Composable
fun PaymentSuccessScreen(redirectUrl: String, shoppingCartViewModel: ShoppingCartViewModel, notificationViewModel: NotificationViewModel) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        notificationViewModel.getNotification()
        shoppingCartViewModel.success(redirectUrl, context)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Pagamento riuscito!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Grazie per il tuo acquisto.",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
