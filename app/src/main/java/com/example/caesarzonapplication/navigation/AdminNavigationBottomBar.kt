package com.example.caesarzonapplication.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.caesarzonapplication.R

@Composable
fun AdminNavigationBottomBar(navController: NavHostController){
    //selectedIndex: MutableState<Int>, dovrebbe essere un parametro delle fun
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    //Creazione della bottom bar
    NavigationBar(
        containerColor = Color(100,104,208)
    ) {//è un componente che rappresenta la barra inferiore

        NavigationBarItem(
            selected = currentDestination?.route == "home",
            onClick = { navController.navigate("home") },
            icon = {
                Icon(
                    Icons.Filled.Home,
                    contentDescription = stringResource(R.string.home),
                    tint = if (currentDestination?.route == "home") Color(238, 137, 60, 255) else Color.Black)
            }
        )

        NavigationBarItem(
            selected = currentDestination?.route == "addProduct",
            onClick = { navController.navigate("addProduct") },
            icon = { Icon(modifier = Modifier
                .width(22.dp)
                .height(22.dp), painter = painterResource(
                id = R.drawable.addition
            ), contentDescription = stringResource(id = R.string.addProduct),
                tint = if (currentDestination?.route == "addProduct") Color(238, 137, 60, 255) else Color.Black) }
        )


        NavigationBarItem(
            selected = currentDestination?.route == "searchUser",
            onClick = { navController.navigate("searchUser") },
            icon = { Icon(Icons.Filled.Search, contentDescription = stringResource(R.string.searchUser),
                tint = if (currentDestination?.route == "searchUser") Color(238, 137, 60, 255) else Color.Black)
            }

        )

        NavigationBarItem(
            selected = currentDestination?.route == "reports",
            onClick = { navController.navigate("reports")},
            icon = { Icon(Icons.Filled.Info, contentDescription = stringResource(R.string.reports),
                tint = if (currentDestination?.route == "reports") Color(238, 137, 60, 255) else Color.Black)
            }
        )

        NavigationBarItem(
            selected = currentDestination?.route == "supportRequest",
            onClick = { navController.navigate("supportRequest")},
            icon = { Icon(modifier = Modifier
                .width(27.dp)
                .height(27.dp), painter = painterResource(id = R.drawable.customer_service), contentDescription = stringResource(R.string.supportRequest),
                tint = if (currentDestination?.route == "supportRequest") Color(238, 137, 60, 255) else Color.Black)
            }
        )
    }
}



