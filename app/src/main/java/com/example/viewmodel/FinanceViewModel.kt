package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Account
import com.example.data.FinanceDatabase
import com.example.data.TransactionRecord
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FinanceViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = FinanceDatabase.getDatabase(application).financeDao()

    val accounts: StateFlow<List<Account>> = dao.getAllAccounts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val transactions: StateFlow<List<TransactionRecord>> = dao.getAllTransactions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalIncome: StateFlow<Double?> = dao.getTotalIncome()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalExpenses: StateFlow<Double?> = dao.getTotalExpenses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun addAccount(account: Account) {
        viewModelScope.launch {
            dao.insertAccount(account)
        }
    }

    fun addTransaction(transaction: TransactionRecord) {
        viewModelScope.launch {
            dao.insertTransaction(transaction)
            
            // Note: in a real app, adding an expense should deduct from the account balance.
            // For simplicity in this demo, we'll calculate totals dynamically or assume balance updates happen.
        }
    }
}
