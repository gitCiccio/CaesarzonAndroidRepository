package com.example.caesarzonapplication.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.caesarzonapplication.R

@Composable
fun NavigationBottomBar(navController: MutableIntState){
    //selectedIndex: MutableState<Int>, dovreppe essere un parametro delle fun
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    //selectedIndex, variabile che ci permette di cambiare pagine dalla home
    //si tratta di una variabile di cui ci interessa salvare lo stato
    //per salvare lo stato ci basterebbe by remember {  mutableStateOf(0) }
    //Ma c'è il problema della ricomposizione, esempio, se ruoto il telefono perdo lo stato della variabile
    //rememberSaveable questo mi permette di tenere traccia dello stato, ci sono altre soluzioni?Sicuramente, ma questa penso sia più comoda
    // var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    //Creazione della bottom bar
    BottomAppBar {//è un componente che rappresenta la barra inferiore

        //Elementi della barra di navigazione
        NavigationBarItem(
            selected = selectedIndex == 0,
            onClick = {
                selectedIndex = 0
                navController.navigate("home") },
            icon = {
                Icon(Icons.Filled.Home, contentDescription = stringResource(R.string.home))
            })

        NavigationBarItem(
            selected = selectedIndex == 1,
            onClick = {
                selectedIndex = 1
                navController.navigate("userInfo") },
            icon = { Icon(Icons.Filled.AccountCircle, contentDescription = stringResource(R.string.userInfo)) }
        )

        NavigationBarItem(
            selected = selectedIndex == 2,
            onClick = {
                selectedIndex = 2
                navController.navigate("shopcart") },
            icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = stringResource(R.string.shopcart)) }
        )

        NavigationBarItem(
            selected = selectedIndex == 3,
            onClick = {
                selectedIndex = 3
                      navController.navigate("settings")},
            icon = { Icon(Icons.Filled.Menu, contentDescription = stringResource(R.string.settings)) })
    }
}

