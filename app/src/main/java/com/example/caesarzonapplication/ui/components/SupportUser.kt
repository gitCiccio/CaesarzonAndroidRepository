package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.caesarzonapplication.model.dto.SupportDTO
import com.example.caesarzonapplication.model.viewmodels.adminViewModels.SupportRequestViewModel

@Composable
fun SupportUser(supports: SupportDTO, supportRequestViewmModel: SupportRequestViewModel){

    var responseText by rememberSaveable { mutableStateOf("") }
    var showPopup by rememberSaveable { mutableStateOf(false) }

    if(showPopup){
        GenericMessagePopup(message = "Risposta inviata con successo", onDismiss = {showPopup = false})
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ){
        Column(
            modifier = Modifier.padding(16.dp)
        ){
            Text(modifier = Modifier.align(Alignment.CenterHorizontally), text = supports.username, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = supports.text, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = supports.dateRequest, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = responseText,
                onValueChange = { responseText = it },
                label = { Text("Rispondi") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { /*onSendResponse(responseText)*/ }),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    supportRequestViewmModel.deleteSupport(supports.id, responseText)
                    responseText=""
                    showPopup = true
                },

                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Invia risposta")
            }
        }
    }
}
//Implementare logica per inviare la risposta