package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscription_requests")
data class SubscriptionRequest(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val customerName: String,
    val phone: String,
    val planPrice: String,
    val planDuration: String,
    val paymentMethod: String,
    val referenceId: String,
    val status: String = "An atant", // "An atant", "Apwouve", "Rejte"
    val createdAt: String
)
