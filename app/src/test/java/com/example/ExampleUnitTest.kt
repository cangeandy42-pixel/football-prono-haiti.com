package com.example

import com.example.data.model.MatchItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun matchItem_titleFormatting_isCorrect() {
        val match = MatchItem(
            homeTeam = "Real Madrid",
            awayTeam = "Barcelona",
            league = "La Liga",
            matchTime = "15:00",
            pickAnalysis = "Gwo match",
            prediction = "1",
            confidence = "Wo",
            visibility = "both",
            updatedAt = "Today",
            odds = 2.10
        )
        assertEquals("Real Madrid vs Barcelona", match.title)
        assertTrue(match.odds > 1.0)
    }

    @Test
    fun ticketCalculation_isCorrect() {
        val odds = listOf(1.50, 2.00, 1.80)
        val combinedOdds = odds.fold(1.0) { acc, o -> acc * o }
        assertEquals(5.4, combinedOdds, 0.01)

        val stake = 250.0
        val potentialGain = stake * combinedOdds
        assertEquals(1350.0, potentialGain, 0.01)
    }
}
