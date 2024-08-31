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

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row () {
            Row(
            ) {
                if (product.image != null) {
                    Image(
                        bitmap = product.image.asImageBitmap(),
                        contentDescription = "Immagine del prodotto",
                        modifier = Modifier
                            .size(200.dp)
                            .padding(5.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No Image", fontSize = 14.sp, color = Color.Gray)
                    }
                }
            }
            Column(
                modifier = Modifier.padding(horizontal = 15.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth()
                        .clickable { navController.navigate("productDetails/${product.product.id}") },
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = product.product.name,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically

                ) {

                    if (product.product.size != null) {
                        Text(
                            text = "Taglia:",
                            style = TextStyle(fontSize = 16.sp)
                        )

                        Button(
                            onClick = { expanded = !expanded },
                            modifier = Modifier.padding(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(
                                    0xFFFFA500
                                )
                            )
                        ) {
                            Text(
                                selectedSize,
                                style = TextStyle(fontSize = 14.sp, color = Color.Black)
                            )
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


                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quantità:",
                        style = TextStyle(fontSize = 16.sp),
                    )

                    IconButton(
                        onClick = {
                            if (quantity > 1) {
                                quantity -= 1
                                shoppingCartViewModel.saveForLaterOrChangeQuantityAndSize(
                                    product.product.id, 1, selectedSize, quantity
                                )
                            }
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(id = R.drawable.minus),
                            contentDescription = "Decrement quantity",

                            )
                    }

                    TextField(
                        value = quantity.toString(),
                        onValueChange = { newQuantity ->
                            val qty = newQuantity.toIntOrNull()
                            if (qty != null && qty > 0) {
                                quantity = qty
                                shoppingCartViewModel.saveForLaterOrChangeQuantityAndSize(
                                    product.product.id, 1, selectedSize, quantity
                                )
                            }
                        },
                        textStyle = TextStyle(fontSize = 16.sp),
                        modifier = Modifier
                            .width(85.dp)
                            .padding(horizontal = 8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        )
                    )

                    IconButton(
                        onClick = {
                            quantity += 1
                            shoppingCartViewModel.saveForLaterOrChangeQuantityAndSize(
                                product.product.id, 1, selectedSize, quantity
                            )
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(id = R.drawable.plus),
                            contentDescription = "Increment quantity"
                        )
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))


                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                    IconButton(
                        onClick = { shoppingCartViewModel.deleteProductCart(product.product.id) },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.trash_bin),
                            contentDescription = "cancel_product"
                        )
                    }
                    Spacer(modifier = Modifier.width(15.dp))
                    Button(
                        onClick = {
                            shoppingCartViewModel.saveForLaterOrChangeQuantityAndSize(
                                product.product.id,
                                0,
                                selectedSize,
                                quantity
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                    ) {
                        Text(text = if (product.product.buyLater) "Sposta nel carrello" else "Salva per dopo")
                    }
                }

            }
        }

    }
}
