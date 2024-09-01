package com.example.caesarzonapplication.ui.components

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
import com.example.caesarzonapplication.model.dto.notificationDTO.UserNotificationDTO
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.NotificationViewModel
import kotlinx.coroutines.flow.StateFlow

@Composable
fun NotificationsPopup(
    notificationViewModel: NotificationViewModel,
    notifications: StateFlow<List<UserNotificationDTO>>, onDismissRequest: () -> Unit) {

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
                if (notifications.value.isEmpty()) {
                    item {
                        Text(text = "Non ci sono notifiche disponibili.")
                    }
                } else {
                    items(notifications.value) { notification ->
                        when (notification) {
                            //is AdminNotificationDTO -> AdminNotificationTab(notification, notificationViewModel)
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