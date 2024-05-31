package com.example.caesarzonapplication.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun SubMenuFloatingButton(icon: ImageVector, onClick: () -> Unit){
    FloatingActionButton(onClick = onClick) {
        Icon(icon, contentDescription="iconaMenuFluttuante")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuFloatingButton(){
    var isMenuExpanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ){
        AnimatedVisibility(
            visible = isMenuExpanded,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            SubMenuFloatingButton(
                icon = Icons.Default.Favorite,
                onClick = {}
            )
        }
        AnimatedVisibility(
            visible = isMenuExpanded,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            SubMenuFloatingButton(
                icon = Icons.Default.LocationOn,
                onClick = { /* Handle navigation */ }
            )
        }
        AnimatedVisibility(
            visible = isMenuExpanded,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            SubMenuFloatingButton(
                icon = Icons.Default.Face,
                onClick = { /* Handle navigation */ }
            )
        }
        AnimatedVisibility(
            visible = isMenuExpanded,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            SubMenuFloatingButton(
                icon = Icons.Default.AccountBox,
                onClick = { /* Handle navigation */ }
            )
        }
    }

    // Main floating action button
    FloatingActionButton(onClick = { isMenuExpanded = !isMenuExpanded }) {
        Icon(Icons.Filled.Star, contentDescription = null)
    }
}