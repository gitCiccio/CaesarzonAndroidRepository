package com.example.caesarzonapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.caesarzonapplication.model.Ban
import com.example.caesarzonapplication.model.Report
import com.example.caesarzonapplication.model.SupportRequest
import com.example.caesarzonapplication.model.User
import com.example.caesarzonapplication.model.service.KeycloakService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminInfoViewModel : ViewModel() {
    private val _searchResults = MutableStateFlow<List<User>>(emptyList())
    val searchResults: StateFlow<List<User>> get() = _searchResults

    private val _reports = MutableStateFlow<List<Report>>(emptyList())
    val reports: StateFlow<List<Report>> get() = _reports

    private val _supportRequests = MutableStateFlow<List<SupportRequest>>(emptyList())
    val supportRequests: StateFlow<List<SupportRequest>> get() = _supportRequests

    private val _bans = MutableStateFlow<List<Ban>>(emptyList())
    val bans: StateFlow<List<Ban>> get() = _bans

    fun searchUsers(query: String) {
        viewModelScope.launch {
            try {
                val users = KeycloakService.searchUsers(query)
                _searchResults.value = users
            } catch (e: Exception) {
                e.printStackTrace()
                _searchResults.value = emptyList()
            }
        }
    }

    //da cambiare

    // Metodo per simulare il caricamento dei dati
    init {
        loadSampleData()
    }

    private fun loadSampleData() {
        viewModelScope.launch {
            // Caricamento dati simulato
            _reports.value = listOf(
                Report("R001", "User1", "Spam", "2023-06-01", listOf("Action1", "Action2")),
                Report("R002", "User2", "Abuse", "2023-06-02", listOf("Action1", "Action2"))
            )
            _supportRequests.value = listOf(
                SupportRequest("SR001", "User1", "Technical", "2023-06-01", listOf("Action1", "Action2")),
                SupportRequest("SR002", "User2", "Billing", "2023-06-02", listOf("Action1", "Action2"))
            )
            _bans.value = listOf(
                Ban("2023-06-01", "User1", "Violation of rules", listOf("Action1", "Action2")),
                Ban("2023-06-02", "User2", "Spamming", listOf("Action1", "Action2"))
            )
        }
    }
}