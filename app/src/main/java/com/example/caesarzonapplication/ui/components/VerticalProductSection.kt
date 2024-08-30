import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.model.dto.productDTOS.ProductSearchWithImage
import com.example.caesarzonapplication.ui.components.ProductCard


@Composable
fun VerticalProductSection(title: String, products: List<ProductSearchWithImage>, navController: NavHostController){
    Spacer(modifier = Modifier.height(15.dp))
    LazyColumn {
            items(products){ product ->
                ProductCard(product = product.product, product.image, navController)
                Spacer(modifier = Modifier.height(15.dp))
            }
    }
}


