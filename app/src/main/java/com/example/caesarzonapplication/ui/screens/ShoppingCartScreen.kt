package com.example.caesarzonapplication.ui.screens

//import com.example.caesarzonapplication.model.viewmodels.ShoppingCartViewModel
//import com.example.caesarzonapplication.ui.components.ShoppingCartCard
/*
@Composable
fun ShoppingCartScreen(navController: NavHostController, logged: MutableState<Boolean>, productsViewModel: ProductsViewModel) {

    val shoppingCartViewModel = ShoppingCartViewModel()
    var showLoginDialog by rememberSaveable { mutableStateOf(false) }
    val newProducts = productsViewModel.newProducts
    val buyLaterProducts = shoppingCartViewModel.buyLaterProducts

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (shoppingCartViewModel.productInShoppingCart.isEmpty()) {
            item {
                EmptyShoppingCart()
            }
        } else {
            items(shoppingCartViewModel.productInShoppingCart) {
                ShoppingCartCard(it, shoppingCartViewModel, navController)
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
                    Button(onClick = { if(!logged.value){showLoginDialog = true} },
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
        if(shoppingCartViewModel.buyLaterProducts.isNotEmpty()) {
            item {
                HorizontalProductSection(title = "Prodotti da comprare più tardi", buyLaterProducts, navController)
            }
        }
        item {
            HorizontalProductSection(title = "Altri prodotti", newProducts, navController)
        }
    }
}*/

