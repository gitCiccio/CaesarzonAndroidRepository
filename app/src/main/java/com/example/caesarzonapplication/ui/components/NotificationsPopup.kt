package com.example.caesarzonapplication.ui.components




import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.caesarzonapplication.model.dto.UserNotificationDTO


@Composable
fun NotificationsPopup(notifications: List<UserNotificationDTO>, onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 24.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Notifications", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.fillMaxWidth() .align(Alignment.CenterHorizontally))
                Spacer(modifier = Modifier.height(8.dp))
                if (notifications.isEmpty()) {
                    Text(text = "No notifications available.")
                } else {
                    notifications.forEach { notification ->
                        Text(text = notification.explanation, modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onDismissRequest) {
                    Text("Close")
                }
            }
        }
    }
}
