package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.R
import com.example.caesarzonapplication.model.viewmodels.ShoppingCartViewModel



@Composable
fun ShoppingCartCard(product: Product, shoppingCartViewModel: ShoppingCartViewModel, navController: NavHostController) {

    Card(modifier = Modifier
        .height(280.dp)
        .width(380.dp)
    ){
        Row(Modifier.clickable { navController.navigate("productDetails/${product.name}")  }) {
            Image(painter = shoppingCartViewModel.getProduct(name = product.name)?.imageRes?.let { painterResource(id = it) } ?: painterResource(id = R.drawable.ic_launcher_background), contentDescription = "foto_del_prodotto",
                Modifier
                    .size(150.dp)
                    .padding(10.dp)
                    .clickable { navController.navigate("productDetails/${product.name}")  }
            )
            Text(text = product.name, fontSize = 30.sp, modifier = Modifier
                .padding(top = 30.dp)
                .clickable { navController.navigate("productDetails/${product.name}")  }
            )
        }

        Row{
            IconButton(onClick = { shoppingCartViewModel.deleteProduct(product.name) },
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .height(70.dp)
                    .width(50.dp)) {
                Icon(painter = painterResource(id = R.drawable.trash_bin), contentDescription = "cancel_product")
                Text(text = "Cancel", fontSize = 10.sp, modifier = Modifier.padding(top = 70.dp))
            }
            Spacer(modifier = Modifier.padding(horizontal = 60.dp))
            IconButton(onClick = { shoppingCartViewModel.decreaseProduct(product) }, modifier = Modifier.width(25.dp)) {
                Icon(painter = painterResource(id = R.drawable.minus), contentDescription = null)
            }

            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            Text(text = if((shoppingCartViewModel.getProduct(product.name)?.quantity ?: 0) > 10)
            {"10+"}
            else
            {shoppingCartViewModel.getProduct(product.name)?.quantity.toString()}, fontSize = 40.sp, modifier = Modifier.padding(top= 2.dp))
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))

            IconButton(onClick = { shoppingCartViewModel.increaseProduct(product) }, modifier = Modifier.width(25.dp)) {
                if(product.quantity<10)
                    Icon(painter = painterResource(id = R.drawable.plus), contentDescription = null)
            }
        }

        Button(modifier = Modifier.padding(horizontal = 110.dp).padding(bottom = 20.dp), onClick = { shoppingCartViewModel.addLaterProduct(product); shoppingCartViewModel.deleteProduct(product.name) }) {
            Text(text = "Salva per dopo")
        }

    }
}