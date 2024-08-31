package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.model.dto.userDTOS.UserFindDTO
import com.example.caesarzonapplication.model.viewmodels.adminViewModels.SearchAndBanUsersViewModel
import com.example.caesarzonapplication.navigation.DetailsScreen

@Composable
fun UserSearchComponent(
    navController: NavHostController,
    searchAndBanUsersViewModel: SearchAndBanUsersViewModel

) {
    val searchResults by searchAndBanUsersViewModel.searchResults.collectAsState()
    var selectedUser by remember { mutableStateOf<UserFindDTO?>(null) }

    Column {
        if (searchResults.isEmpty()) {
            Text(
                modifier = Modifier
                    .padding(top = 150.dp)
                    .padding(horizontal = 80.dp),
                text = "Nessun utente trovato",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
        } else {
            searchResults.forEach { foundUser ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            selectedUser = if (selectedUser == foundUser) null else foundUser
                        },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (foundUser.profileImage != null) {
                        RoundedImage(
                            bitmap = foundUser.profileImage.asImageBitmap(),
                            contentDescription = "Profile Image",
                            modifier = Modifier.size(50.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile Image",
                            modifier = Modifier.size(50.dp),
                            tint = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(
                        text = foundUser.username,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleMedium
                    )

                    if (selectedUser == foundUser) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            IconButton(onClick = { /*Naviga verso il profilo dell'utente*/ }) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Dati Profilo Utente"
                                )
                            }
                            IconButton(onClick = { /*Naviga verso l'elenco degli indirizzi dell'utente*/ }) {
                                Icon(
                                    imageVector = Icons.Default.AddLocation,
                                    contentDescription = "Indirizzi Utente"
                                )
                            }
                            IconButton(onClick = { /*Naviga verso i metodi di pagamento dell'utente*/ }) {
                                Icon(
                                    imageVector = Icons.Default.AddCard,
                                    contentDescription = "Metodi di Pagamento Utente"
                                )
                            }
                            IconButton(onClick = { /*Naviga verso il carrello dell'utente*/ }) {
                                Icon(
                                    imageVector = Icons.Default.AddShoppingCart,
                                    contentDescription = "Carrello Utente"
                                )
                            }
                            IconButton(onClick = {
                                searchAndBanUsersViewModel.banUser(foundUser)
                                selectedUser = null
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Banna Utente"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RoundedImage(
    bitmap: ImageBitmap,
    contentDescription: String,
    modifier: Modifier = Modifier,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    borderWidth: Dp = 2.dp,
    cornerRadius: Dp = 15.dp
) {
    Image(
        bitmap = bitmap,
        contentDescription = contentDescription,
        modifier = modifier
            .size(50.dp)
            .clip(RoundedCornerShape(cornerRadius))
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(cornerRadius)
            )
    )
}