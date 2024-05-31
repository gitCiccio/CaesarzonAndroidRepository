package com.example.caesarzonapplication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.caesarzonapplication.navigation.AppNavigation
import com.example.caesarzonapplication.ui.components.AppTopBar
import com.example.caesarzonapplication.ui.components.MenuFloatingButton
import com.example.caesarzonapplication.ui.components.NavigationBottomBar
import com.example.caesarzonapplication.ui.theme.CaesarzonApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CaesarzonApplicationTheme {
                AppNavigation()
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

