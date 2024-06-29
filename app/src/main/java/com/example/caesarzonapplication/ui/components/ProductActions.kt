import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caesarzonapplication.ui.components.GenericMessagePopup

@Composable
fun ProductActions() {

    var showPopup by remember { mutableStateOf(false) }

    if(showPopup){
        GenericMessagePopup(message = "Prodotto aggiunto al carrello con successo!", onDismiss = {showPopup = false})
    }

    Row {
        Button(
            onClick = { /* Azione per acquistare subito */ },
            modifier = Modifier.weight(1f).height(48.dp)
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
            modifier = Modifier.weight(1f).height(48.dp)
        ) {
            Text(
                "Aggiungi al carrello",
                style = TextStyle(fontSize = 14.sp, color = Color.White),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.width(8.dp))


        Button(
            onClick = { /* Azione per aggiungere ai desideri */ },
            modifier = Modifier.weight(1f).height(48.dp)
        ) {
            Text(
                "Aggiungi alla lista desideri",
                style = TextStyle(fontSize = 14.sp, color = Color.White),
                textAlign = TextAlign.Center
            )
        }
    }
}
