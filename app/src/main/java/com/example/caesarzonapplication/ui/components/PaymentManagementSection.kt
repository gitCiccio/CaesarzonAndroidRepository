package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.caesarzonapplication.model.dto.userDTOS.CardDTO
import com.example.caesarzonapplication.model.utils.CardUtils
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.CardsViewModel

@Composable
fun PaymentManagementSection(cardsViewModel: CardsViewModel) {

    var cardNumber by rememberSaveable { mutableStateOf("") }
    var cardHolderName by rememberSaveable { mutableStateOf("") }
    var expirationDate by rememberSaveable { mutableStateOf("") }
    var cvc by rememberSaveable { mutableStateOf("") }
    val paymentMethods by cardsViewModel.cards.collectAsState()
    var showAddPaymentDialog by rememberSaveable { mutableStateOf(false) }
    var showRemovePaymentDialog by rememberSaveable { mutableStateOf(false) }
    var errorMessage by rememberSaveable { mutableStateOf<String?>(null) }
    val cardUtils = CardUtils()

    LaunchedEffect(Unit) {
        cardsViewModel.loadCards()
    }

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
            AddPaymentButton { showAddPaymentDialog = true }
            RemovePaymentButton { showRemovePaymentDialog = true }
        }
    }

    if (showAddPaymentDialog) {
        AddPaymentDialog(
            cardNumber = cardNumber,
            onCardNumberChange = { cardNumber = formatCardNumber(it) },
            cardHolderName = cardHolderName,
            onCardHolderNameChange = { cardHolderName = it },
            expirationDate = expirationDate,
            onExpirationDateChange = { expirationDate = it},
            cvc = cvc,
            onCvcChange = { cvc = it },
            onSave = {
                if (cardUtils.validateCreditCardDetails(cardHolderName, cardNumber, expirationDate, cvc)) {
                    val sanitizedCardNumber = cardNumber.replace(" ", "")
                    cardsViewModel.addCard(CardDTO("", sanitizedCardNumber, cardHolderName, cvc, expirationDate, 0.0))
                    showAddPaymentDialog = false
                    errorMessage = null
                } else {
                    errorMessage = "Dati della carta non validi"
                }
            },
            onDismiss = { showAddPaymentDialog = false },
            errorMessage = errorMessage
        )
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
fun AddPaymentButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Text(text = "Aggiungi metodo di pagamento")
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
fun AddPaymentDialog(
    cardNumber: String,
    onCardNumberChange: (String) -> Unit,
    cardHolderName: String,
    onCardHolderNameChange: (String) -> Unit,
    expirationDate: String,
    onExpirationDateChange: (String) -> Unit,
    cvc: String,
    onCvcChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
    errorMessage: String?
) {
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
                DialogTitle("Aggiungi Metodo di Pagamento", onDismiss)
                Spacer(modifier = Modifier.height(16.dp))
                PaymentTextField(value = cardHolderName, onValueChange = onCardHolderNameChange, label = "Nome e Cognome del Titolare")
                PaymentTextField(value = cardNumber, onValueChange = onCardNumberChange, label = "Numero Carta di Credito (#### #### #### ####)")
                PaymentTextField(value = expirationDate, onValueChange = onExpirationDateChange, label = "Data di Scadenza (AAAA-MM)")
                PaymentTextField(value = cvc, onValueChange = onCvcChange, label = "CVC")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onSave) {
                    Text(text = "Salva")
                }
                errorMessage?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = it,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentTextField(value: String, onValueChange: (String) -> Unit, label: String) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color(0xFFF7F7F7)
        )
    )
}

fun formatCardNumber(input: String): String {
    val sanitizedInput = input.filter { it.isDigit() }
    return sanitizedInput.chunked(4).joinToString(" ").take(19)
}