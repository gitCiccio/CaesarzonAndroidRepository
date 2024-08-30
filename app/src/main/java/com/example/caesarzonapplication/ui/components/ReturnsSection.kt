import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.caesarzonapplication.model.dto.productDTOS.OrderDTO
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.globalUsername
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.OrdersViewModel

@Composable
fun ReturnsSection(orderViewModels: OrdersViewModel) {


    val refoundOrders by orderViewModels.refoundOrders.collectAsState()
    LaunchedEffect(Unit) {
        orderViewModels.getOrdersFromServer()
    }
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
            items(refoundOrders) { refoundOrder ->
                OrderItem(
                    order = refoundOrder.orderNumber,
                    isExpanded = expandedOrder == refoundOrder.id,
                    onOrderClick = {
                        expandedOrder = if (expandedOrder == refoundOrder.id) null else refoundOrder.id
                    },
                    orderViewModels,
                    refoundOrder.id
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun OrderItem(
    order: String,
    isExpanded: Boolean,
    onOrderClick: () -> Unit,
    orderViewModels: OrdersViewModel,
    orderId: String
) {
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
                OrderDetails(order = order, orderViewModels = orderViewModels, orderId)
            }
        }
    }
}

@Composable
fun OrderDetails(order: String, orderViewModels: OrdersViewModel, orderId: String) {
    val productCardDTOList by orderViewModels.productCardDTOList.collectAsState()
    LaunchedEffect(Unit) {
        orderViewModels.getProductInOrder(orderId)
    }
    Column(modifier = Modifier.padding(start = 16.dp)) {
        Text(text = "Dettagli dell'ordine: $order", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        for(product in productCardDTOList){
            Text(text = "Nome prodotto: ${product.name}")
            Text(text = "Quantità: ${product.quantity}")
            Text(text = "Totale: ${product.total}")
            Text(text = "Totale con sconto: ${product.discountTotal}")
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}