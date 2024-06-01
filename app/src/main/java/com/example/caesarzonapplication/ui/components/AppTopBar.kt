package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.caesarzonapplication.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(){
    var textFieldValue by remember { mutableStateOf("")}

    TopAppBar(
        modifier = Modifier
            .background(Color(100, 104, 208))
            .padding(10.dp)
            .height(100.dp), // Distanza dalla barra delle notifiche
        title = {
        TextField(
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            modifier = Modifier
                .padding(10.dp),
            placeholder = { Text(text = "Cerca...")},
            shape = RoundedCornerShape(50),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.LightGray,
                focusedTextColor = Color.Black
            )
        )
        },
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.mini),
                contentDescription = "Caesarzon",
                modifier = Modifier
                    .height(70.dp)
                    .padding(vertical = 4.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(100,104,208)
        )
    )

}