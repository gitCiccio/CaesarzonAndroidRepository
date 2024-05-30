package com.example.caesarzonapplication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caesarzonapplication.ui.theme.Typography
import com.example.caesarzonapplication.viewmodels.AccountInfoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAccountActivity(accountInfoViewModel: AccountInfoViewModel) {
    val accountInfoDataState by accountInfoViewModel.accountInfoData.collectAsState()
    Scaffold(//Scaffold è un componente predefinito di layout, fornisce spazi per elementi come TopBar, BottomBar, contenuto, ecc...
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = "Informazioni dell'account",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
            }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            ), actions = {
                Icon(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo dell'applicazione", // Descrizione per accessibilità, aggiungi una stringa se necessaria
                    tint = Color.White // Colore della tua icona, puoi modificarlo se necessario
                )
            })
        },
        bottomBar = { NavigationBottomBar() },//spazio per la bottom bar
        content = {padding ->
                  Column {
                      Text(
                          text = stringResource(id = R.string.userInfo),
                          style = TextStyle(fontSize = Typography.bodyLarge.fontSize),
                          modifier = Modifier
                              .align(Alignment.CenterHorizontally)
                              .padding(10.dp)
                      )
                      Text(text= accountInfoDataState.name)
                      Text(text= accountInfoDataState.surname)
                      Text(text= accountInfoDataState.username)
                      Text(text= accountInfoDataState.email)

                  }
        },
        floatingActionButton = { MenuFloatingButton() },//spazio per posizionare un componente
        floatingActionButtonPosition = FabPosition.End//specifica dove posizionare il floatingActionButton
    )

}