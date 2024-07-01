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
import com.example.caesarzonapplication.ui.components.GenericMessagePopup
import com.example.caesarzonapplication.ui.components.WishlistPopup
import com.example.caesarzonapplication.viewmodels.AdminViewModels.AdminProductViewModel
import com.example.caesarzonapplication.viewmodels.WishlistViewModel

@Composable
fun ProductActions(productViewModel: AdminProductViewModel) {

    var showPopup by rememberSaveable { mutableStateOf(false) }
    var showWishlistPopup by rememberSaveable { mutableStateOf(false) }
    var isAdmin by rememberSaveable { mutableStateOf(false) }

    if(!isAdmin){
        if (showPopup) {
            GenericMessagePopup(message = "Prodotto aggiunto al carrello con successo!", onDismiss = { showPopup = false })
        }

        if(showWishlistPopup){
            WishlistPopup(wishlistViewModel = WishlistViewModel(), onDismiss = {showWishlistPopup = false})
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { /* Azione per acquistare subito */ },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500))
                ) {
                    Text(
                        "Acquista",
                        style = TextStyle(fontSize = 14.sp, color = Color.White),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { showPopup = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500))
                ) {
                    Text(
                        "Aggiungi al carrello",
                        style = TextStyle(fontSize = 14.sp, color = Color.White),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showWishlistPopup = true},
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
            Button(onClick = { productViewModel.updateProduct() }) {
                Text(text = "Modifica")
            }
            Button(onClick = { productViewModel.deleteProduct() }) {
                Text(text = "Elimina")
            }
        }
    }
}
