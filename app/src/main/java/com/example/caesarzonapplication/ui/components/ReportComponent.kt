package com.example.caesarzonapplication.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.caesarzonapplication.viewmodels.AdminViewModels.ReportViewModel

@Composable
fun ReportComponent(reportViewModel: ReportViewModel, padding: PaddingValues) {
    LazyColumn(modifier = Modifier.padding(padding)) {
        if (!reportViewModel.reports.isEmpty()) {
            items(reportViewModel.reports) { report ->
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