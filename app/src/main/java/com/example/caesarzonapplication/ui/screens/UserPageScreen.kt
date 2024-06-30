package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.R
import com.example.caesarzonapplication.ui.components.WishListComponent

@Composable
fun UserPageContent(padding: PaddingValues) {
    var public by rememberSaveable { mutableStateOf(true) }
    var shared by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally){
        Image(
            painter = painterResource(id = R.drawable.logoutente),
            contentDescription = "profile_pic",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Username", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "nome_utente    cognome_utente", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(30.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(30.dp)){
            Button(onClick = { public=!public }) {
                Text(text = "Liste pubbliche")
            }
            Button(onClick = { shared=!shared ; public=!public}) {
                Text(text = "Liste condivise")
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        if(public)
            WishListComponent()
        if(shared)
            WishListComponent()
    }
}

@Composable
fun UserPageScreen(navController: NavHostController) {
    Scaffold(
        content = { padding ->
            UserPageContent(padding)
        },
        bottomBar = { Spacer(modifier = Modifier.padding(60.dp))}
    )
}