package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.data.TransactionRecord
import com.example.viewmodel.FinanceViewModel
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(viewModel: FinanceViewModel, navController: NavController) {
    var type by remember { mutableStateOf("Expense") }
    var amount by remember { mutableStateOf("") }
    var merchant by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    
    val accounts by viewModel.accounts.collectAsState()
    var selectedAccountId by remember { mutableStateOf(if (accounts.isNotEmpty()) accounts.first().id else 0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Transaction") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = type == "Expense", onClick = { type = "Expense" }, label = { Text("Expense") })
                FilterChip(selected = type == "Income", onClick = { type = "Income" }, label = { Text("Income") })
                FilterChip(selected = type == "Transfer", onClick = { type = "Transfer" }, label = { Text("Transfer") })
            }

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = merchant,
                onValueChange = { merchant = it },
                label = { Text("Merchant / Payee") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth()
            )

            // Simplistic account selection (in a real app this would be a DropdownMenu)
            if (accounts.isNotEmpty()) {
                Text("Selected Account: ${accounts.find { it.id == selectedAccountId }?.name ?: "None"}")
            } else {
                Text("No accounts available. Add one first.", color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val newTx = TransactionRecord(
                        amount = amount.toDoubleOrNull() ?: 0.0,
                        date = Date().time,
                        merchant = merchant,
                        category = category,
                        type = type,
                        accountId = selectedAccountId
                    )
                    viewModel.addTransaction(newTx)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = accounts.isNotEmpty() && amount.isNotBlank()
            ) {
                Text("Save Transaction")
            }
        }
    }
}
