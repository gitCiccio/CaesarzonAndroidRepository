import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caesarzonapplication.model.dto.AdminNotificationDTO
import com.example.caesarzonapplication.model.viewmodels.NotificationViewModel
import java.util.UUID

@Composable
fun AdminNotificationTab(adminNotificationDTO: AdminNotificationDTO, notificationViewModel: NotificationViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            Text(
                text = adminNotificationDTO.admin,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp
            )
            Text(
                text = adminNotificationDTO.date,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp
            )
            Text(
                text = adminNotificationDTO.subject,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp
            )
        }
        IconButton(
            onClick = { /*notificationViewModel.deleteNotification(adminNotificationDTO.id, false)*/ }
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Delete Notification",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}