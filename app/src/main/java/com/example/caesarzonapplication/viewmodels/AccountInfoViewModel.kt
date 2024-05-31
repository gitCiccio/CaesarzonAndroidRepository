package com.example.caesarzonapplication.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AccountInfoData (
    val name: String="",
    val surname: String="",
    val username: String="",
    val email: String=""
)

class AccountInfoViewModel : ViewModel(){
    private val _accountInfoData = MutableStateFlow(AccountInfoData())
    val accountInfoData: StateFlow<AccountInfoData> = _accountInfoData.asStateFlow()

    init {
        retrieveAccountInfo()
    }

    private fun retrieveAccountInfo() {
        TODO("Not yet implemented")
    }
}