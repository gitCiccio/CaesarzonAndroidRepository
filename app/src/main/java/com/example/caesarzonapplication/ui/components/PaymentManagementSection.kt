package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.caesarzonapplication.model.dto.CardDTO
//import com.example.caesarzonapplication.model.utils.CardUtils
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.CardsViewModel

@Composable
fun PaymentManagementSection(cardsViewModel: CardsViewModel) {

    var cardNumber by remember { mutableStateOf("") }
    var cardHolderName by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf("") }
    var cvc by remember { mutableStateOf("") }
    var paymentMethods by rememberSaveable { mutableStateOf(listOf("**** **** **** 1234", "**** **** **** 5678")) }
    var showAddPaymentDialog by rememberSaveable { mutableStateOf(false) }
    var showRemovePaymentDialog by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    //val cardUtils = CardUtils()

    LazyColumn {
        item {
            Text(text = "Metodi di pagamento", style = MaterialTheme.typography.bodyMedium)
            paymentMethods.forEach { method ->
                PaymentMethodItem(method)
            }
            AddPaymentButton { showAddPaymentDialog = true }
            RemovePaymentButton { showRemovePaymentDialog = true }
        }
    }

    if (showAddPaymentDialog) {
        Dialog(onDismissRequest = { showAddPaymentDialog = false }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = MaterialTheme.shapes.medium)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = "Aggiungi Metodo di Pagamento",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Chiudi",
                            modifier = Modifier.clickable { showAddPaymentDialog = false }
                        )
                    }
                    TextField(
                        value = cardHolderName,
                        onValueChange = { cardHolderName = it },
                        label = { Text("Nome e Cognome del Titolare") }
                    )
                    TextField(
                        value = cardNumber,
                        onValueChange = { cardNumber = it },
                        label = { Text("Numero Carta di Credito") }
                    )
                    TextField(
                        value = expirationDate,
                        onValueChange = { expirationDate = it },
                        label = { Text("Data di Scadenza (AAAA-MM)") }
                    )
                    TextField(
                        value = cvc,
                        onValueChange = { cvc = it },
                        label = { Text("CVC") }
                    )
                    Button(onClick = {
                        val card = CardDTO("",cardNumber, cardHolderName, cvv = cvc ,expirationDate, balance = 0.0)
                        cardsViewModel.addCard(card)
                        /*if (cardUtils.validateCreditCardDetails(cardNumber.text, expirationDate.text, cvc.text)) {
                            val maskedCardNumber = cardUtils.maskCreditCard(cardNumber.text)
                            paymentMethods = paymentMethods + maskedCardNumber
                            showAddPaymentDialog = false
                            errorMessage = null
                            val card = CardDTO("",maskedCardNumber, cardHolderName.text, expirationDate.text, cvc.text, balance = 0.0)
                            cardsViewModel.addCard(card)
                        } else {
                            errorMessage = "Dati della carta non validi"
                        }*/
                    }) {
                        Text(text = "Salva")
                    }
                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage!!,
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
    if (showRemovePaymentDialog) {
        RemovePaymentDialog(
            paymentMethods = paymentMethods,
            onRemove = { method -> paymentMethods = paymentMethods - method },
            onDismiss = { showRemovePaymentDialog = false }
        )
    }
}

@Composable
fun PaymentMethodItem(method: String) {
    //val cardUtils = CardUtils()
    //Text(
        //text = cardUtils.maskCreditCard(method),
        //modifier = Modifier.padding(8.dp)
    //)
}
@Composable
fun AddPaymentButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = "Aggiungi metodo di pagamento")
    }
}
@Composable
fun RemovePaymentButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = "Rimuovi metodo di pagamento")
    }
}
@Composable
fun RemovePaymentDialog(paymentMethods: List<String>, onRemove: (String) -> Unit, onDismiss: () -> Unit) {
    //val cardUtils = CardUtils()

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
                    .background(Color.White, shape = MaterialTheme.shapes.medium)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "Rimuovi Metodo di Pagamento",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Chiudi",
                        modifier = Modifier.clickable { onDismiss() }
                    )
                }
                paymentMethods.forEach { method ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        //Text(text = cardUtils.maskCreditCard(method), modifier = Modifier.weight(1f))
                        IconButton(onClick = { onRemove(method) }) {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Rimuovi"
                            )
                        }
                    }
                }
            }
        }
    }
}