package com.example.caesarzonapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.ui.components.NavigationBottomBar
import com.example.caesarzonapplication.viewmodels.AccountInfoViewModel
import kotlinx.coroutines.launch

@Composable
fun UserRegistrationScreen(padding: PaddingValues, navController: NavHostController, logged: Boolean) {
    val accountInfoViewModel = AccountInfoViewModel()
    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Scaffold(
        topBar = {},
        bottomBar = { NavigationBottomBar(navController, logged) },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Registrazione",
                            fontSize = 24.sp,
                            modifier = Modifier
                                .padding(20.dp),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        TextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Nome") },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        TextField(
                            value = surname,
                            onValueChange = { surname = it },
                            label = { Text("Cognome") },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        TextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Username") },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        TextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        TextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Conferma password") },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            if (password != confirmPassword) {
                                password = ""
                                confirmPassword = ""
                            } else {
                                coroutineScope.launch {
                                    println(accountInfoViewModel.registerUser(
                                        name,
                                        surname,
                                        username,
                                        email,
                                        password
                                    ))
                                }
                            } },
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .padding(20.dp),
                        colors = ButtonDefaults.buttonColors()
                    )
                    {
                        Text("Registrati")
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    )
}
