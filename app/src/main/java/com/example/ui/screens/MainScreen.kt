package com.example.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.viewmodel.FinanceViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Home", Icons.Filled.Dashboard)
    object Transactions : Screen("transactions", "Transact", Icons.Filled.ReceiptLong)
    object AddTransaction : Screen("add_transaction", "Add", Icons.Filled.Add)
    object Analytics : Screen("analytics", "Analytics", Icons.Filled.Timeline)
    object Accounts : Screen("accounts", "Accounts", Icons.Filled.AccountBalanceWallet)
    object AddAccount : Screen("add_account", "Add Account", Icons.Filled.Add)
}

val bottomNavItems = listOf(
    Screen.Dashboard,
    Screen.Transactions,
    Screen.AddTransaction,
    Screen.Analytics,
    Screen.Accounts
)

@Composable
fun MainScreen(viewModel: FinanceViewModel = viewModel()) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            // Hide bottom bar on secondary screens
            if (currentDestination?.route in bottomNavItems.map { it.route }) {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        if (screen == Screen.AddTransaction) {
                            NavigationBarItem(
                                icon = {
                                    FloatingActionButton(onClick = {
                                        navController.navigate(Screen.AddTransaction.route)
                                    }) {
                                        Icon(screen.icon, contentDescription = screen.title)
                                    }
                                },
                                label = { Text(screen.title) },
                                selected = false,
                                onClick = {
                                    navController.navigate(screen.route)
                                }
                            )
                        } else {
                            NavigationBarItem(
                                icon = { Icon(screen.icon, contentDescription = screen.title) },
                                label = { Text(screen.title) },
                                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) { DashboardScreen(viewModel, navController) }
            composable(Screen.Accounts.route) { AccountsScreen(viewModel, navController) }
            composable(Screen.AddAccount.route) { AddAccountScreen(viewModel, navController) }
            composable(Screen.Transactions.route) { TransactionsScreen(viewModel, navController) }
            composable(Screen.AddTransaction.route) { AddTransactionScreen(viewModel, navController) }
            composable(Screen.Analytics.route) { AnalyticsScreen(viewModel, navController) }
        }
    }
}
