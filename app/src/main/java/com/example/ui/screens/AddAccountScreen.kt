package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.data.Account
import com.example.viewmodel.FinanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountScreen(viewModel: FinanceViewModel, navController: NavController) {
    var name by remember { mutableStateOf("") }
    var bankName by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Bank") } // "Bank" or "Credit Card"
    var balance by remember { mutableStateOf("") }
    var outstanding by remember { mutableStateOf("") }
    var lastFour by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Account") },
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
                FilterChip(
                    selected = type == "Bank",
                    onClick = { type = "Bank" },
                    label = { Text("Bank Account") }
                )
                FilterChip(
                    selected = type == "Credit Card",
                    onClick = { type = "Credit Card" },
                    label = { Text("Credit Card") }
                )
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Account Custom Nickname") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = bankName,
                onValueChange = { bankName = it },
                label = { Text("Bank / Financial Institution") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = lastFour,
                onValueChange = { lastFour = it },
                label = { Text("Account Number (Last 4 digits)") },
                modifier = Modifier.fillMaxWidth()
            )

            if (type == "Bank") {
                OutlinedTextField(
                    value = balance,
                    onValueChange = { balance = it },
                    label = { Text("Opening Balance") },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                OutlinedTextField(
                    value = balance,
                    onValueChange = { balance = it },
                    label = { Text("Credit Limit") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = outstanding,
                    onValueChange = { outstanding = it },
                    label = { Text("Outstanding Balance") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val newAccount = Account(
                        name = name,
                        bankName = bankName,
                        type = type,
                        lastFour = lastFour,
                        balance = balance.toDoubleOrNull() ?: 0.0,
                        outstanding = outstanding.toDoubleOrNull() ?: 0.0,
                        availableCredit = if (type == "Credit Card") (balance.toDoubleOrNull() ?: 0.0) - (outstanding.toDoubleOrNull() ?: 0.0) else 0.0
                    )
                    viewModel.addAccount(newAccount)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Account")
            }
        }
    }
}
