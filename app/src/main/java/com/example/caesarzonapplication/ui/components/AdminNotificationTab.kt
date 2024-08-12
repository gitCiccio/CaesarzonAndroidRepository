import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caesarzonapplication.model.dto.AdminNotificationDTO
import com.example.caesarzonapplication.model.viewmodels.HomeViewModel
import java.util.UUID

@Composable
fun AdminNotificationTab(adminNotificationDTO: AdminNotificationDTO, homeViewModel: HomeViewModel) {
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
                text = adminNotificationDTO.admin,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp
            )
            Text(
                text = adminNotificationDTO.date,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = adminNotificationDTO.subject,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp
            )
        }
        ClickableText(
            text = AnnotatedString("X"),
            onClick = { homeViewModel.deleteNotification(adminNotificationDTO.id, false) },
            style = LocalTextStyle.current.copy(
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.error
            )
        )
    }
}
