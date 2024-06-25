import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.caesarzonapplication.model.User
import com.example.caesarzonapplication.viewmodels.AccountInfoViewModel

@Composable
fun UserSearchSection(accountInfoViewModel: AccountInfoViewModel) {
    var searchText by remember { mutableStateOf("") }
    val users by accountInfoViewModel.searchResults.collectAsState()

    Column {
        // Campo di testo per l'inserimento del testo di ricerca
        TextField(
            value = searchText,
            onValueChange = {
                searchText = it
                accountInfoViewModel.searchUsers(searchText) // Esegui la ricerca ogni volta che il testo cambia
            },
            label = { Text("Cerca utenti") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(users) { user ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = user.username)
                    Row {
                        Button(onClick = { /* Gestisci follow/unfollow */ }) {
                            Text(text = if (user.isFollower) "Unfollow" else "Follow")
                        }
                        Button(onClick = { /* Gestisci aggiungi/rimuovi amico */ }) {
                            Text(text = if (user.isFriend) "Remove Friend" else "Add Friend")
                        }
                    }
                }
            }
        }
    }
}
