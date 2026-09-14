package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val type: String, // "Bank" or "Credit Card"
    val bankName: String,
    val balance: Double, // Opening balance or credit limit
    val availableCredit: Double = 0.0, // For credit cards
    val outstanding: Double = 0.0, // For credit cards
    val lastFour: String
)
