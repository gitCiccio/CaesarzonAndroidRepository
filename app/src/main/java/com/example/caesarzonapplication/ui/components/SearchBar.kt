package com.example.caesarzonapplication.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.R
import com.example.caesarzonapplication.navigation.DetailsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(navController: NavHostController){
    var textFieldValue by rememberSaveable { mutableStateOf("")}

    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Color(100, 104, 208))
            .padding(8.dp),
        title = {
            TextField(
                value = textFieldValue,
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 18.sp
                ),
                onValueChange = { textFieldValue = it },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .height(56.dp)
                    .fillMaxWidth()
                ,
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
                        navController.navigate(DetailsScreen.ProductSearchResultsScreen.route+"/$textFieldValue")
                               println(textFieldValue)},
                    onDone = {
                        textFieldValue = ""
                    }
                ),
                singleLine = true,
                trailingIcon = {
                    IconButton(
                        onClick = {
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