package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.viewmodel.FinanceViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(viewModel: FinanceViewModel, navController: NavController) {
    val transactions by viewModel.transactions.collectAsState()

    val formatCurrency = { amount: Double ->
        NumberFormat.getCurrencyInstance(Locale("en", "IN")).format(amount)
    }

    val formatDate = { date: Long ->
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(date))
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Transactions") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(transactions) { tx ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(tx.merchant, style = MaterialTheme.typography.titleMedium)
                            Text(tx.category, style = MaterialTheme.typography.bodyMedium)
                            Text(formatDate(tx.date), style = MaterialTheme.typography.labelSmall)
                        }
                        Column {
                            Text(
                                text = if (tx.type == "Expense") "-${formatCurrency(tx.amount)}" else "+${formatCurrency(tx.amount)}",
                                style = MaterialTheme.typography.titleMedium,
                                color = if (tx.type == "Expense") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                            )
                            Text(tx.type, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
    }
}
