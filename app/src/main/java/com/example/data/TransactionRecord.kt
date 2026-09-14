package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val date: Long,
    val merchant: String,
    val category: String,
    val type: String, // "Expense", "Income", "Transfer", "Card Payment"
    val accountId: Int, // The account this belongs to
    val toAccountId: Int? = null, // For transfers and card payments
    val notes: String = ""
)
