package com.example.caesarzonapplication.ui.components

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
import com.example.caesarzonapplication.model.dto.UserNotificationDTO
import com.example.caesarzonapplication.viewmodels.HomeViewModel
import java.util.UUID

@Composable
fun UserNotificationsTab(userNotificationDTO: UserNotificationDTO, homeViewModel: HomeViewModel) {
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
        ClickableText(
            text = AnnotatedString("X"),
            onClick = { homeViewModel.deleteNotification(userNotificationDTO.id, true) },
            style = LocalTextStyle.current.copy(
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.error
            )
        )
    }
}
