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
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.model.dto.userDTOS.AddressDTO
import com.example.caesarzonapplication.model.dto.userDTOS.CardDTO
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.ShoppingCartViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.AddressViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.CardsViewModel
import com.example.caesarzonapplication.ui.components.LoadBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.internal.wait

import android.util.Log
import com.example.caesarzonapplication.navigation.BottomBarScreen
import com.example.caesarzonapplication.navigation.DetailsScreen
import kotlinx.coroutines.delay

@Composable
fun CheckoutScreen(
    navController: NavHostController,
    shoppingCartViewModel: ShoppingCartViewModel,
    addressViewModel: AddressViewModel,
    cardsViewModel: CardsViewModel
) {
    val addresses by addressViewModel.addresses.collectAsState()
    val cards by cardsViewModel.cards.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val shoppingCartProducts by shoppingCartViewModel.productsInShoppingCart.collectAsState()

    val total by shoppingCartViewModel.total.collectAsState()

    val context = LocalContext.current

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            cardsViewModel.loadCards()
            addressViewModel.loadAddresses()
        } finally {
            coroutineScope.launch {
                delay(500)
                isLoading = false
            }
        }
    }

    var selectedAddress by remember { mutableStateOf<AddressDTO?>(null) }
    var selectedCard by remember { mutableStateOf<CardDTO?>(null) }
    var payPal by remember { mutableStateOf(false) }

    if (isLoading) {
        Log.d("CheckoutScreen", "Displaying LoadBar")
        LoadBar()
    } else {
        Log.d("CheckoutScreen", "Displaying checkout content")
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Text(text = "Seleziona un indirizzo", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                if (addresses.isEmpty()) {
                    navController.navigate(DetailsScreen.CheckOutScreen.route)
                    Log.d("CheckoutScreen", "No addresses found")
                    Button(
                        onClick = {
                            navController.navigate(BottomBarScreen.Profile.route)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors()
                    ) {
                        Text(text = "Aggiungi Indirizzo")
                    }
                } else {
                    Log.d("CheckoutScreen", "Displaying addresses")
                    addresses.forEach { address ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = selectedAddress == address,
                                onClick = {
                                    selectedAddress = address
                                    Log.d("CheckoutScreen", "Selected address: $selectedAddress")
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "${address.roadType} ${address.roadName} ${address.houseNumber} ${address.city.city} ${address.city.cap} ${address.city.province}")
                        }
                    }
                }
            }

            item {
                Text(text = "Seleziona un metodo di pagamento", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                if (cards.isEmpty()) {
                    Log.d("CheckoutScreen", "No cards found")
                    Button(
                        onClick = { /* Logica per aggiungere una carta */ },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors()
                    ) {
                        Text(text = "Aggiungi Carta")
                    }
                } else {
                    Log.d("CheckoutScreen", "Displaying cards")
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
                                    Log.d("CheckoutScreen", "Selected card: $selectedCard, PayPal: $payPal")
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "${card.owner} ${card.cardNumber} ${card.expiryDate}")
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
                            Log.d("CheckoutScreen", "PayPal selected: $payPal")
                        }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Riepilogo Ordine", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))


            }

            item {
                shoppingCartProducts.forEach { prod ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {

                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "${prod.product.name} x ${prod.product.quantity} | ${prod.product.size}"
                            )
                            Text(
                                text = "Prezzo: ${prod.product.total} \n" +
                                        "Sconto: ${prod.product.discountTotal} \n" +
                                        "Prezzo finale: ${prod.product.total - prod.product.discountTotal}"
                            )
                            Text(
                                text =  "---------------------------------"
                            )
                        }
                    }
                }
            }

            item {
                Text(text = "Totale ordine: €${total}", style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            navController.navigate("shopcart")
                            Log.d("CheckoutScreen", "Navigating to shop cart")
                        },
                        colors = ButtonDefaults.buttonColors(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Torna al carrello", color = Color.White)
                    }

                    Button(
                        onClick = {
                            Log.d("CheckoutScreen", "Purchase initiated")
                            Log.d("CheckoutScreen", "Selected address: $selectedAddress, Selected card: $selectedCard, PayPal: $payPal")
                            if (selectedAddress != null && (selectedCard != null || payPal)) {
                                Log.d("CheckoutScreen", "Proceeding with purchase")
                                shoppingCartViewModel.purchase(
                                    selectedAddress?.id ?: "",
                                    selectedCard?.id ?: "",
                                    payPal,
                                    context
                                )
                            } else {
                                Log.d("CheckoutScreen", "Error: Conditions not met for purchase")
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
}
