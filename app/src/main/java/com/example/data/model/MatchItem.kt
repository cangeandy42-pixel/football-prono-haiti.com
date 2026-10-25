package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matches")
data class MatchItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val homeTeam: String,
    val awayTeam: String,
    val league: String,
    val matchTime: String,
    val pickAnalysis: String,
    val prediction: String,
    val confidence: String, // "Wo", "Mwayen", "Ba"
    val visibility: String, // "public", "premium", "both"
    val updatedAt: String,
    val status: String = "Ap vini", // "Ap vini", "Genyen", "Pèdi", "Fini"
    val odds: Double = 1.85
) {
    val title: String get() = "$homeTeam vs $awayTeam"
}
