import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.caesarzonapplication.ui.components.EmptyShoppingCart
import com.example.caesarzonapplication.ui.components.HorizontalProductSection
import com.example.caesarzonapplication.ui.components.NavigationBottomBar
import com.example.caesarzonapplication.ui.components.ProductCard
import com.example.caesarzonapplication.viewmodels.HomeViewModel
import com.example.caesarzonapplication.viewmodels.ProductsViewModel

@Composable
fun ShoppingCartScreen(padding: PaddingValues, shoppingCartViewModel: ProductsViewModel, homeViewModel: HomeViewModel) {
    Scaffold(
        topBar = {},
        bottomBar = { NavigationBottomBar(navController = rememberNavController()) },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                if(shoppingCartViewModel.productInShoppingCart.isEmpty()){
                    item {
                        EmptyShoppingCart()
                    }
                } else {
                    items(shoppingCartViewModel.productInShoppingCart){ product ->
                            ProductCard(product = product)
                            Spacer(modifier = Modifier.height(15.dp))
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
                item {
                    HorizontalProductSection(title = "Altri prodotti", products = homeViewModel.products)
                }
            }
        }
    )
}
