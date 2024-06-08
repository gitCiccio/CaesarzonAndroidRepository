import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.caesarzonapplication.ui.components.AppTopBar
import com.example.caesarzonapplication.ui.components.EmptyShoppingCart
import com.example.caesarzonapplication.ui.components.HorizontalProductSection
import com.example.caesarzonapplication.ui.components.LoginPopup
import com.example.caesarzonapplication.ui.components.NavigationBottomBar
import com.example.caesarzonapplication.ui.components.ShoppingCartCard
import com.example.caesarzonapplication.viewmodels.HomeViewModel
import com.example.caesarzonapplication.viewmodels.ShoppingCartViewModel

@Composable
fun ShoppingCartScreen(
    padding: PaddingValues,
    shoppingCartViewModel: ShoppingCartViewModel,
    homeViewModel: HomeViewModel,
    navController: NavHostController,
    logged: Boolean,
) {
    var showLoginDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column {
                Spacer(modifier = Modifier.height(45.dp))
                AppTopBar(navController)
            }
        },
        bottomBar = { NavigationBottomBar(navController, logged = false) },
        content = { padding ->
            if(showLoginDialog) {
                LoginPopup(
                    onDismiss = { showLoginDialog = false },
                    onLoginSuccess = { showLoginDialog = false;},
                    navController = navController
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center, // Centra la LazyColumn verticalmente
                horizontalAlignment = Alignment.CenterHorizontally // Centra la LazyColumn orizzontalmente
            ) {
                if (shoppingCartViewModel.productInShoppingCart.isEmpty()) {
                    item {
                        EmptyShoppingCart()
                    }
                } else {
                    items(shoppingCartViewModel.productInShoppingCart) { it ->
                        ShoppingCartCard(it, shoppingCartViewModel)
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                    item {
                        Row {
                            Button(onClick = { navController.navigate("home") },
                                modifier = Modifier
                                    .padding(15.dp)
                                    .height(60.dp)
                                    .wrapContentWidth(Alignment.Start),
                                colors = buttonColors(
                                    containerColor = Color.Gray
                                )
                            )
                            {
                                Text(text = "Continua a comprare",
                                    style = TextStyle(fontSize = 16.sp),
                                    softWrap = false
                                )
                            }
                            Button(onClick = { if(!logged){showLoginDialog = true} },
                                modifier = Modifier
                                    .padding(15.dp)
                                    .weight(1f)
                                    .height(60.dp),
                                colors = buttonColors(
                                    containerColor = Color.Green
                                ))
                            {
                                Text(text = "Procedi",
                                    style = TextStyle(fontSize = 20.sp)
                                )
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
                item {
                    HorizontalProductSection(title = "Prodotti salvati per dopo", products = homeViewModel.products)
                }
                item {
                    HorizontalProductSection(title = "Altri prodotti", products = homeViewModel.products)
                }
            }
        }
    )
}
