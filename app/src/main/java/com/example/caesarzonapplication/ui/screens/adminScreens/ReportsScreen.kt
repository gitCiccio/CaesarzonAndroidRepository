package com.example.caesarzonapplication.ui.screens.adminScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caesarzonapplication.model.viewmodels.adminViewModels.AdminReportViewModel
import com.example.caesarzonapplication.ui.components.ReportCardTab


@Composable
fun ReportsScreen(reportViewModel: AdminReportViewModel) {

    val reports by reportViewModel.reports.collectAsState()
    LaunchedEffect(Unit) {
        reportViewModel.loadReports()
    }


    Column {
        Box(
            Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
        ){
            Text(
                text = "Segnalazioni",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(top = 30.dp),
                color = Color.Black,
            )
        }
        LazyColumn(modifier = Modifier.padding())
        {
            if (reports.isNotEmpty()) {
                items(reports) { report ->
                    ReportCardTab(report, reportViewModel)
                }
            } else {
                item {
                    Text(
                        modifier = Modifier
                            .padding(top = 150.dp)
                            .padding(horizontal = 80.dp),
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        text = "Non ci sono segnalazioni"
                    )
                }
            }
        }
    }
}



