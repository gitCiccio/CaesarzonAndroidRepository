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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.caesarzonapplication.ui.theme.CaesarzonApplicationTheme

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
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")//serve perché non ho gestito l'innerpadding, mi secca
@Composable//indica che è una funzione composable, produce un albero di elementi, definiti dentro la funzione
fun MyApp(){
    Scaffold(//Scaffold è un componente predefinito di layout, fornisce spazi per elementi come TopBar, BottomBar, contenuto, ecc...
        topBar = { AppTopBar() },//spazio per posizionare la topBar
        bottomBar = { NavigationBottomBar() },//spazio per la bottom bar
        content = {},//spazio per il contenuto principale
        floatingActionButton = { MenuFloatingButton() },//spazio per posizionare un componente
        floatingActionButtonPosition = FabPosition.End//specifica dove posizionare il floatingActionButton
    )

}

//Barra di navigazione inferiore
@Composable
fun NavigationBottomBar(){

    //selectedIndex, variabile che ci permette di cambiare pagine dalla home
    //si tratta di una variabile di cui ci interessa salvare lo stato
    //per salvare lo stato ci basterebbe by remember {  mutableStateOf(0) }
    //Ma c'è il problema della ricomposizione, esempio, se ruoto il telefono perdo lo stato della variabile
    //rememberSaveable questo mi permette di tenere traccia dello stato, ci sono altre soluzioni?Sicuramente, ma questa penso sia più comoda
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    //Creazione della bottom bar
    BottomAppBar {//è un componente che rappresenta la barra inferiore

        //Elementi della barra di navigazione
        NavigationBarItem(
            selected = selectedIndex == 0,//Indica se l'elemento di navigazione è selezionato
            onClick = { selectedIndex = 0 },//Funzione che viene eseguita, quando si clicca l'elemento, per ora cambia solo l'indice
            icon = { Icon(Icons.Filled.Home, contentDescription = null) },//Icona associata all'elemento di navigazione
        )

        NavigationBarItem(
            selected = selectedIndex == 1,
            onClick = { selectedIndex = 1},
            icon = { Icon(Icons.Filled.AccountCircle, contentDescription = null)}
        )

        NavigationBarItem(
            selected = selectedIndex == 2,
            onClick = { selectedIndex = 2},
            icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = null)}
        )

        NavigationBarItem(
            selected = selectedIndex == 3,
            onClick = { selectedIndex = 3},
            icon = { Icon(Icons.Filled.Menu, contentDescription = null)})
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
fun AppTopBar(){
    var textFieldValue by remember { mutableStateOf("") }

    TopAppBar(
        title = { Text(text = "Caesarzon")},
        navigationIcon = {
            TextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it},
                modifier = Modifier
                    .padding(8.dp),
                placeholder = { Text(text = "Cerca...")}
            )
        }
    )
}