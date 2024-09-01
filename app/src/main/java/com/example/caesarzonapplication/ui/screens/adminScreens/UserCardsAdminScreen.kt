package com.example.caesarzonapplication.ui.screens.adminScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.caesarzonapplication.model.dto.userDTOS.CardDTO
import com.example.caesarzonapplication.model.utils.CardUtils
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AccountInfoViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.CardsViewModel

@Composable
fun UserCardsAdminScreen(cardsViewModel: CardsViewModel) {

    val paymentMethods by cardsViewModel.userCardsForAdmin.collectAsState()
    var showRemovePaymentDialog by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        item {
            Text(
                text = "Metodi di pagamento",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            paymentMethods.forEach { method ->
                PaymentMethodItem(method.cardNumber)
            }
            Spacer(modifier = Modifier.height(16.dp))
            RemovePaymentButton { showRemovePaymentDialog = true }
        }
    }

    if (showRemovePaymentDialog) {
        RemovePaymentDialog(
            paymentMethods = paymentMethods,
            onDismiss = { showRemovePaymentDialog = false },
            cardsViewModel
        )
    }
}

@Composable
fun PaymentMethodItem(method: String) {
    val cardUtils = CardUtils()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = cardUtils.maskCreditCard(method),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
        )
    }
}

@Composable
fun RemovePaymentButton(onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
    ) {
        Text(text = "Rimuovi metodo di pagamento")
    }
}

@Composable
fun RemovePaymentDialog(paymentMethods: List<CardDTO>, onDismiss: () -> Unit, cardsViewModel: CardsViewModel) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DialogTitle("Rimuovi Metodo di Pagamento", onDismiss)
                Spacer(modifier = Modifier.height(16.dp))
                paymentMethods.forEach { method ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = method.cardNumber,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = {
                            cardsViewModel.deleteCard(method)
                        }) {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Rimuovi",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(24.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DialogTitle(title: String, onDismiss: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.weight(1f)
        )
        Icon(
            Icons.Filled.Close,
            contentDescription = "Chiudi",
            modifier = Modifier.clickable { onDismiss() }
        )
    }
}