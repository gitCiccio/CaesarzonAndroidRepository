package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.caesarzonapplication.model.dto.userDTOS.AddressDTO
import com.example.caesarzonapplication.model.dto.userDTOS.CardDTO
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.ShoppingCartViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AddressViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.CardsViewModel

@Composable
fun CheckoutScreen(shoppingCartViewModel: ShoppingCartViewModel, addressViewModel: AddressViewModel, cardsViewModel: CardsViewModel) {

    val addresses by addressViewModel.addresses.collectAsState()
    val cards by cardsViewModel.cards.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) {

        addressViewModel.resetAddresses()
        cardsViewModel.resetCards()

        addressViewModel.loadAddresses()
        cardsViewModel.loadCards()
    }

    var selectedAddress by remember { mutableStateOf<AddressDTO?>(null) }
    var selectedCard by remember { mutableStateOf<CardDTO?>(null) }
    var payPal by remember { mutableStateOf(false) }

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(text = "Seleziona un indirizzo", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            if (cards.isEmpty()) {
                Button(
                    onClick = { /* Logica per aggiungere una carta */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors()
                ) {
                    Text(text = "Aggiungi Indirizzo")
                }
            } else {
                addresses.forEach { address ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedAddress == address ,
                            onClick = {
                                selectedAddress = address
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = address.toString())
                    }
                }
            }
        }

        item {
            Text(text = "Seleziona un metodo di pagamento", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            if (cards.isEmpty()) {
                Button(
                    onClick = { /* Logica per aggiungere una carta */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors()
                ) {
                    Text(text = "Aggiungi Carta")
                }
            } else {
                cards.forEach { card ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = selectedCard == card && !payPal,
                            onClick = {
                                selectedCard = card
                                payPal = false
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = card.toString())
                    }
                }
            }
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = "Oppure paga con PayPal",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                RadioButton(
                    selected = payPal,
                    onClick = {
                        payPal = true
                        selectedCard = null
                    }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Text(text = "Riepilogo Ordine", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            //Text(text = "Totale: €${"%.2f".format(shoppingCartViewModel.total.toString())}", style = MaterialTheme.typography.bodyLarge)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { /* Torna al carrello */ },
                    colors = ButtonDefaults.buttonColors(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Torna al carrello", color = Color.White)
                }

                Button(
                    onClick = {
                        if (selectedAddress != null && (selectedCard != null || payPal)) {
                            selectedAddress?.id?.let { selectedCard?.let { it1 -> shoppingCartViewModel.purchase(it, it1.id, payPal, context) } }
                        } else {
                            // Mostra un messaggio di errore o avviso
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(),
                    enabled = selectedAddress != null && (selectedCard != null || payPal)
                ) {
                    Text(text = "Procedi all'acquisto", color = Color.White)
                }
            }
        }
    }
}
