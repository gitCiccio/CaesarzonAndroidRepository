package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caesarzonapplication.R
import com.example.caesarzonapplication.model.Product
import com.example.caesarzonapplication.viewmodels.ShoppingCartViewModel


@Composable
fun ShoppingCartCard(product: Product, shoppingCartViewModel: ShoppingCartViewModel) {

    var quantity by rememberSaveable { mutableIntStateOf(1) }

    Card(modifier = Modifier
        .height(320.dp)
        .width(380.dp)
    ){
        Row {
            Image(painter = painterResource(id =product.imageRes), contentDescription = "foto_del_prodotto",
                Modifier.size(200.dp))
            Text(text = product.name, fontSize = 30.sp, modifier = Modifier.padding(top = 30.dp))
        }

        Row{
            IconButton(onClick = { shoppingCartViewModel.deleteProduct(product.name) },
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .height(70.dp)
                    .width(50.dp)) {
                Icon(painter = painterResource(id = R.drawable.trash_bin), contentDescription = "cancel_product")
            }

            Spacer(modifier = Modifier.padding(70.dp))
            Row(modifier = Modifier.padding(top = 8.dp)){
                IconButton(onClick = { if(quantity>1) quantity-=1 }, modifier = Modifier.width(25.dp)) {
                    Icon(painter = painterResource(id = R.drawable.minus), contentDescription = null)
                }

                Spacer(modifier = Modifier.padding(8.dp))
                Text(text = if (quantity<10) "${quantity}" else "10+", fontSize = 40.sp, modifier = Modifier.padding(top= 2.dp))
                Spacer(modifier = Modifier.padding(8.dp))

                IconButton(onClick = { if(quantity<10) quantity+=1 }, modifier = Modifier.width(25.dp)) {
                    if(quantity<10)
                        Icon(painter = painterResource(id = R.drawable.plus), contentDescription = null)
                }
            }
        }

    }
}