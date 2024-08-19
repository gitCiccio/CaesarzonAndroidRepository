package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.caesarzonapplication.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(navController: NavHostController){
    var textFieldValue by rememberSaveable { mutableStateOf("")}

    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(67.dp)
            .background(Color(100, 104, 208))
            .padding(8.dp),
        title = {
            TextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .height(50.dp),
                placeholder = { Text(
                    text = "Cerca...",
                    color = Color.Black,
                    modifier = Modifier
                        .alpha(0.5f)
                        .padding(horizontal = 15.dp)
                )},
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
                        navController.navigate("ProductSearchResultsScreen/${textFieldValue}")
                    }
                ),
                singleLine = true,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            textFieldValue = ""
                            navController.navigate("ProductSearchResultsScreen/${textFieldValue}")
                        },
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .height(50.dp)
                            .width(50.dp)
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
                                .padding(horizontal = 10.dp)
                                .height(50.dp)
                                .width(50.dp)
                                .background(
                                    color = Color.LightGray,
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

@Composable
@Preview
fun SearchBarPreview(){
    SearchBar(navController = NavHostController(LocalContext.current))
}