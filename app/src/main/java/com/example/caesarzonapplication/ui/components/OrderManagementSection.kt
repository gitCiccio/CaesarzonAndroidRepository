package com.example.caesarzonapplication.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OrderManagementSection() {

    val orders = listOf(
        "Ordine #1",
        "Ordine #2",
        "Ordine #3",
        "Ordine #4",
        "Ordine #5",
        "Ordine #6",
        "Ordine #7",
        "Ordine #8",
        "Ordine #9",
        "Ordine #10"
    )

    var expandedOrder by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
    {
        Text(
            text = "Cronologia ordini",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )

        LazyColumn (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            items(orders) { order ->
                OrderItem(
                    order = order,
                    isExpanded = expandedOrder == order,
                    onOrderClick = {
                        expandedOrder = if (expandedOrder == order) null else order
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun OrderItem(order: String, isExpanded: Boolean, onOrderClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onOrderClick() }
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(10.dp)
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = order,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = null
                )
            }
            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                OrderDetails(order = order)
            }
        }
    }
}

@Composable
fun OrderDetails(order: String) {
    Column(modifier = Modifier.padding(start = 16.dp)) {
        Text(text = "Dettagli dell'ordine: $order", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Prodotto 1: Descrizione, Prezzo, Quantità")
        Text(text = "Prodotto 2: Descrizione, Prezzo, Quantità")
        Button(
            onClick = { //richiedi reso
                 },
            modifier = Modifier
                .padding(top = 16.dp)
                .clip(MaterialTheme.shapes.medium),
            shape = MaterialTheme.shapes.medium,
        ) {
            Text(text = "Richiedi reso")
        }
    }
}

