package com.example.caesarzonapplication.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.caesarzonapplication.R

@Composable
fun NotificationFloatingButton(onClick : () -> Unit){
    FloatingActionButton(onClick = onClick, containerColor = Color(100,104,208)) {
        Icon(Icons.Filled.Notifications, contentDescription = stringResource(id = R.string.notifications))
    }
}