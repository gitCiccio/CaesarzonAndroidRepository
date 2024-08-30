import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.model.dto.productDTOS.ProductDTO
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.isAdmin
import com.example.caesarzonapplication.ui.components.GenericMessagePopup
import com.example.caesarzonapplication.ui.components.WishlistPopup
import com.example.caesarzonapplication.model.viewmodels.adminViewModels.AdminProductViewModel
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.WishlistViewModel
import com.example.caesarzonapplication.navigation.BottomBarScreen
import com.example.caesarzonapplication.navigation.DetailsScreen
import com.google.gson.Gson

@Composable
fun ProductActions(navController: NavHostController, adminProductViewModel: AdminProductViewModel, wishlistViewModel: WishlistViewModel, productDTO: ProductDTO) {

    var showPopup by rememberSaveable { mutableStateOf(false) }
    var showWishlistPopup by rememberSaveable { mutableStateOf(false) }

    if(!isAdmin.value){
        if (showPopup) {
            GenericMessagePopup(message = "Prodotto aggiunto al carrello con successo!", onDismiss = { showPopup = false })
        }

        if(showWishlistPopup){
            WishlistPopup(wishlistViewModel = wishlistViewModel, productId = productDTO.id,onDismiss = {showWishlistPopup = false})
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { /* Azione per acquistare subito */ },
                    modifier = Modifier
                        .weight(1f)
                        .height(72.dp)
                        .padding(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500))
                ) {
                    Text(
                        "Acquista",
                        style = TextStyle(fontSize = 14.sp, color = Color.White),
                        textAlign = TextAlign.Center
                    )
                }
                Button(
                    onClick = { showPopup = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(72.dp)
                        .padding(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500))
                ) {
                    Text(
                        "Aggiungi al carrello",
                        style = TextStyle(fontSize = 14.sp, color = Color.White),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Button(
                onClick = {
                    showWishlistPopup = true},
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(48.dp)
                    .fillMaxWidth(0.5f)
            ) {
                Text(
                    "Aggiungi alla lista desideri",
                    style = TextStyle(fontSize = 14.sp, color = Color.White),
                    textAlign = TextAlign.Center
                )
            }
        }
    }else{
        Row{
            /*Button(onClick = {
                val gson = Gson()
                val productJson = gson.toJson(productDTO)
                navController.navigate("addProduct/$productJson") }
            ) {
                Text(text = "Modifica")
            }*/
            Button(
                onClick = {
                    adminProductViewModel.deleteProduct(productDTO.id)
                    navController.navigate(BottomBarScreen.Home.route)
                }
            ) {
                Text(text = "Elimina")
            }
        }
    }
}
