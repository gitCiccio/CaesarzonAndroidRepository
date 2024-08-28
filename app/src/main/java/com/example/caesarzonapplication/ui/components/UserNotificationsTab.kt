package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caesarzonapplication.model.dto.UserNotificationDTO
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.NotificationViewModel

@Composable
fun UserNotificationsTab(userNotificationDTO: UserNotificationDTO, notificationViewModel: NotificationViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(
                text = userNotificationDTO.user,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp
            )
            Text(
                text = userNotificationDTO.date,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = userNotificationDTO.subject,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = userNotificationDTO.explanation,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp
            )
        }
        IconButton(onClick = {
            notificationViewModel.deleteNotification(userNotificationDTO)}) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete Notification",
                tint = Color.Red // Colore dell'icona, opzionale
            )
        }
    }
}
