package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.R
import com.example.caesarzonapplication.model.dto.productDTOS.ProductCartWithImage
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.ShoppingCartViewModel


@Composable
fun ShoppingCartCard(
    product: ProductCartWithImage,
    shoppingCartViewModel: ShoppingCartViewModel,
    navController: NavHostController
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedSize by remember { mutableStateOf(product.product.size ?: "") }
    var quantity by remember { mutableStateOf(product.product.quantity) }
    var buyLater by remember { mutableStateOf(product.product.buyLater) }

    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("productDetails/${product.product.id}") },
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (product.image != null) {
                    Image(
                        bitmap = product.image.asImageBitmap(),
                        contentDescription = "Immagine del prodotto",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = 10.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No Image", fontSize = 16.sp, color = Color.Gray)
                    }
                }

                Text(
                    text = product.product.name,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(end = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { shoppingCartViewModel.deleteProductCart(product.product.id) },
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.trash_bin),
                        contentDescription = "cancel_product"
                    )
                }

                if (product.product.size != null) {
                    Text(
                        text = "Taglia:",
                        style = TextStyle(fontSize = 16.sp)
                    )

                    Box {
                        Button(
                            onClick = { expanded = !expanded },
                            modifier = Modifier.padding(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500))
                        ) {
                            Text(selectedSize, style = TextStyle(fontSize = 14.sp, color = Color.White))
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            listOf("XS", "S", "M", "L", "XL").forEach { size ->
                                DropdownMenuItem(
                                    text = { Text(text = size) },
                                    onClick = {
                                        selectedSize = size
                                        expanded = false
                                        shoppingCartViewModel.saveForLaterOrChangeQuantityAndSize(
                                            product.product.id, 1, size, quantity
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Quantità:",
                    style = TextStyle(fontSize = 16.sp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (quantity > 1) {
                                quantity -= 1
                                shoppingCartViewModel.saveForLaterOrChangeQuantityAndSize(
                                    product.product.id, 1, selectedSize, quantity
                                )
                            }
                        },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.minus), // Devi avere un'icona "minus"
                            contentDescription = "Decrement quantity"
                        )
                    }

                    Text(
                        text = quantity.toString(),
                        style = TextStyle(fontSize = 16.sp),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    IconButton(
                        onClick = {
                            quantity += 1
                            shoppingCartViewModel.saveForLaterOrChangeQuantityAndSize(
                                product.product.id, 1, selectedSize, quantity
                            )
                        },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.plus), // Devi avere un'icona "plus"
                            contentDescription = "Increment quantity"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = Modifier.align(Alignment.End),
                onClick = {
                    buyLater = product.product.buyLater
                    shoppingCartViewModel.saveForLaterOrChangeQuantityAndSize(
                        product.product.id,
                        0,
                        selectedSize,
                        quantity
                    )
                }
            ) {
                Text(text = if (buyLater) "Sposta nel carrello" else "Salva per dopo")
            }
        }
    }
}
