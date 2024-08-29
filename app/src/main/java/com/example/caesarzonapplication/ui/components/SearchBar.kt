package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.R
import com.example.caesarzonapplication.model.service.KeycloakService.Companion.logged
import com.example.caesarzonapplication.model.viewmodels.userViewmodels.NotificationViewModel
import com.example.caesarzonapplication.navigation.DetailsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(navController: NavHostController, notificationViewModel: NotificationViewModel){
    var textFieldValue by rememberSaveable { mutableStateOf("")}
    var showNotificationsPopup by rememberSaveable { mutableStateOf(false) }
    val notifications by notificationViewModel.userNotification.collectAsState()
    var unreadNotifications by remember { mutableIntStateOf(0) }
    var searchBarWidth by rememberSaveable { mutableIntStateOf(250) }

    LaunchedEffect(notifications) {
        notificationViewModel.getNotification()
        unreadNotifications = notifications.count { !it.read }
    }

    TopAppBar(
        modifier = Modifier
            .height(70.dp)
            .background(Color(100, 104, 208))
            .padding(8.dp),
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopStart
            )
            {
                TextField(
                    value = textFieldValue,
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp
                    ),
                    onValueChange = { textFieldValue = it },
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .width(searchBarWidth.dp)
                        .height(56.dp),
                    placeholder = {
                        Text(
                            text = "Cerca...",
                            color = Color.Black,
                            modifier = Modifier
                                .alpha(0.5f)
                        ) },
                    shape = RoundedCornerShape(50),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.LightGray,
                        focusedTextColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if (textFieldValue.isNotEmpty())
                                navController.navigate(DetailsScreen.ProductSearchResultsScreen.route+"/$textFieldValue") },
                        onDone = {
                            textFieldValue = ""
                        }
                    ),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (textFieldValue.isNotEmpty())
                                    navController.navigate(DetailsScreen.ProductSearchResultsScreen.route+"/$textFieldValue")},
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .size(50.dp)
                                .background(
                                    color = Color.LightGray,
                                    shape = RoundedCornerShape(50)
                                )
                        ){
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.Black,
                                modifier = Modifier
                                    .size(35.dp)
                                    .background(
                                        color = Color.Gray,
                                        shape = RoundedCornerShape(50)
                                    )
                            )
                        }
                    }
                )
                if (logged.value) {
                    searchBarWidth = 215
                    Box(modifier = Modifier.align(Alignment.TopEnd)) {
                        FloatingActionButton(
                            onClick = {
                                showNotificationsPopup = !showNotificationsPopup
                                if (showNotificationsPopup) {
                                    notificationViewModel.getNotification()
                                    notificationViewModel.updateNotification()
                                }
                                unreadNotifications = 0
                            },
                            containerColor = Color(255, 200, 0, 255),
                            contentColor = Color.White,
                            shape = CircleShape,
                            modifier = Modifier
                                .size(48.dp),
                            elevation = FloatingActionButtonDefaults.elevation(
                                defaultElevation = 2.dp,
                                pressedElevation = 4.dp
                            )
                        ){
                            Icon(Icons.Filled.Notifications, contentDescription = "Notifications")

                            if (unreadNotifications > 0) {
                                Box(
                                    contentAlignment = Alignment.TopEnd,
                                    modifier = Modifier
                                        .size(18.dp)
                                        .background(Color.Red, shape = CircleShape)
                                        .align(Alignment.TopEnd)
                                        .padding(end = 6.dp, top = 6.dp)
                                )
                                {
                                    Text(
                                        text = unreadNotifications.toString(),
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        modifier = Modifier.align(Alignment.TopEnd)
                                    )
                                }
                            }
                        }
                    }

                    if (showNotificationsPopup) {
                        NotificationsPopup(
                            notificationViewModel = notificationViewModel,
                            notifications = notificationViewModel.userNotification,
                            onDismissRequest = { showNotificationsPopup = false }
                        )
                    }
                }
            }
        },
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.mini),
                contentDescription = "Caesarzon",
                modifier = Modifier
                    .width(90.dp)
                    .height(80.dp)
                    .padding(vertical = 3.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(100,104,208)
        )
    )
}


