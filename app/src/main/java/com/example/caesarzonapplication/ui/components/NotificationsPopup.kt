package com.example.caesarzonapplication.ui.components

import AdminNotificationTab
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.caesarzonapplication.model.dto.AdminNotificationDTO
import com.example.caesarzonapplication.model.dto.UserNotificationDTO
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.NotificationViewModel

@Composable
fun NotificationsPopup(notifications: List<Any>, onDismissRequest: () -> Unit) {

    val notificationViewModel = NotificationViewModel()

    if (notifications.isEmpty()) {
        return
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 24.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                item {
                    Text(
                        text = "Notifications",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                if (notifications.isEmpty()) {
                    item {
                        Text(text = "Non ci sono notifiche disponibili.")
                    }
                } else {
                    items(notifications) { notification ->
                        when (notification) {
                            is AdminNotificationDTO -> AdminNotificationTab(notification, notificationViewModel)
                            is UserNotificationDTO -> UserNotificationsTab(notification, notificationViewModel)
                            else -> error("Tipo di notifica sconosciuto")
                        }
                    }
                }
                item {
                    Button(onClick = onDismissRequest) {
                        Text("Chiudi")
                    }
                }
            }
        }
    }
}