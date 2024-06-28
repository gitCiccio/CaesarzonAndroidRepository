import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.viewmodels.AdminInfoViewModel


@Composable
fun UserSearchSection(adminInfoViewModel: AdminInfoViewModel) {
    var searchText by remember { mutableStateOf("") }


    Column {
        // Campo di testo per l'inserimento del testo di ricerca
        Row(verticalAlignment = Alignment.CenterVertically){
            Spacer(modifier = Modifier.width(35.dp))
            TextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                },
                label = { Text("Cerca utenti") },
                modifier = Modifier
                    .width(320.dp)

            )
            IconButton(onClick = { adminInfoViewModel.searchSpecifcUsers(searchText)}) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        LazyColumn {
            items(adminInfoViewModel.searchResults) { user ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = user.username)
                    Row {
                        Button(onClick = { /* Handle ban action */ }) {
                            Text(text = "Banna utente")
                        }
                    }
                }
            }
        }
    }
}

