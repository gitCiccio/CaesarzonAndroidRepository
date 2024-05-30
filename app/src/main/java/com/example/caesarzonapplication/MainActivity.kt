package com.example.caesarzonapplication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.caesarzonapplication.ui.theme.CaesarzonApplicationTheme
import kotlin.math.absoluteValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CaesarzonApplicationTheme {
                Surface {
                    MyApp()
                }
            }
        }
    }
}
//"Bootstrap" della prima pagina android
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp(){
    var selectedIndex = remember { mutableIntStateOf(0) }
    Scaffold(
        topBar = { AppTopBar() },
        bottomBar = { NavigationBottomBar(selectedIndex) },
        floatingActionButton = { MenuFloatingButton() },
        floatingActionButtonPosition = FabPosition.End,
        content = { padding -> // Aggiungi il parametro padding
            when (selectedIndex.value) {
                0 -> {
                    // Schermata Home
                    Text("Home Screen", modifier = Modifier.padding(padding)) // Usa il padding
                }
                1 -> {
                    // Schermata Settings
                    UserAccountActivity(selectedIndex = selectedIndex, modifier = Modifier.padding(padding)) // Passa il padding
                }
                2 -> {
                    // Schermata Shopcart
                    Text("Shopcart Screen", modifier = Modifier.padding(padding)) // Usa il padding
                }
                3 -> {
                    Text("Impostazioni", modifier = Modifier.padding(padding)) // Usa il padding
                }
            }
        }
    )
}


//Barra di navigazione inferiore
@Composable
fun NavigationBottomBar(selectedIndex: MutableState<Int>){

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
            selected = selectedIndex.value == 0,
            onClick = { selectedIndex.value = 0},
            icon = {
            Icon(Icons.Filled.Home, contentDescription = stringResource(R.string.home))
        })

        NavigationBarItem(
            selected = selectedIndex.value == 1,
            onClick = { selectedIndex.value = 1},
            icon = { Icon(Icons.Filled.AccountCircle, contentDescription = stringResource(R.string.userInfo))}
        )

        NavigationBarItem(
            selected = selectedIndex.value == 2,
            onClick = { selectedIndex.value = 2},
            icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = stringResource(R.string.shopcart))}
        )

        NavigationBarItem(
            selected = selectedIndex.value == 3,
            onClick = { selectedIndex.value = 3},
            icon = { Icon(Icons.Filled.Menu, contentDescription = stringResource(R.string.settings))})
    }
}

//Creazione del pulasante del menù rapido
//Unit sta per void
@Composable
fun SubMenuFloatingButton(icon: ImageVector, onClick: () -> Unit){
    FloatingActionButton(onClick = onClick) {
        Icon(icon, contentDescription = null)
    }
}

//Menù rapido
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MenuFloatingButton() {
    var isMenuExpanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            // AnimatedVisibility for each submenu item
            AnimatedVisibility(
                visible = isMenuExpanded,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                SubMenuFloatingButton(
                    icon = Icons.Default.Favorite,
                    onClick = {}
                )
            }
            AnimatedVisibility(
                visible = isMenuExpanded,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                SubMenuFloatingButton(
                    icon = Icons.Default.LocationOn,
                    onClick = { /* Handle navigation */ }
                )
            }
            AnimatedVisibility(
                visible = isMenuExpanded,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                SubMenuFloatingButton(
                    icon = Icons.Default.Face,
                    onClick = { /* Handle navigation */ }
                )
            }
            AnimatedVisibility(
                visible = isMenuExpanded,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                SubMenuFloatingButton(
                    icon = Icons.Default.AccountBox,
                    onClick = { /* Handle navigation */ }
                )
            }
        }

        // Main floating action button
        FloatingActionButton(onClick = { isMenuExpanded = !isMenuExpanded }) {
            Icon(Icons.Filled.Star, contentDescription = null)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar() {
    var textFieldValue by remember { mutableStateOf("") }

    TopAppBar(
        title = { Text(text = "Caesarzon") },
        actions = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                OutlinedTextField(
                    value = textFieldValue,
                    onValueChange = { textFieldValue = it },
                    modifier = Modifier
                        .padding(8.dp)
                        .width(200.dp)
                        .clip(RoundedCornerShape(50)), // Shape for rounded corners
                    placeholder = { Text(text = "Cerca...") }
                )
            }
        },
    )
}