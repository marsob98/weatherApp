package com.example.weatherapp.data.remote.model

import org.junit.Assert.*
import org.junit.Test

class UVRecommendationTest {

    @Test
    fun `getRecommendation returns correct info for low UV index`() {
        // Given
        val uvIndex = 2.5

        // When
        val recommendation = UVRecommendation.getRecommendation(uvIndex)

        // Then
        assertEquals("Niski", recommendation.level)
        assertEquals("Niskie zagrożenie z promieniowania UV dla przeciętnej osoby.", recommendation.description)
        assertEquals("Możesz bezpiecznie przebywać na zewnątrz.", recommendation.recommendation)
    }

    @Test
    fun `getRecommendation returns correct info for moderate UV index`() {
        // Given
        val uvIndex = 4.0

        // When
        val recommendation = UVRecommendation.getRecommendation(uvIndex)

        // Then
        assertEquals("Umiarkowany", recommendation.level)
        assertEquals("Umiarkowane ryzyko uszkodzenia skóry z powodu UV.", recommendation.description)
        assertTrue(recommendation.recommendation.contains("Załóż okulary przeciwsłoneczne"))
    }

    @Test
    fun `getRecommendation returns correct info for high UV index`() {
        // Given
        val uvIndex = 7.0

        // When
        val recommendation = UVRecommendation.getRecommendation(uvIndex)

        // Then
        assertEquals("Wysoki", recommendation.level)
        assertEquals("Wysokie ryzyko uszkodzenia skóry z powodu UV.", recommendation.description)
        assertTrue(recommendation.recommendation.contains("Ogranicz przebywanie na słońcu"))
    }

    @Test
    fun `getRecommendation returns correct info for very high UV index`() {
        // Given
        val uvIndex = 9.5

        // When
        val recommendation = UVRecommendation.getRecommendation(uvIndex)

        // Then
        assertEquals("Bardzo wysoki", recommendation.level)
        assertEquals("Bardzo wysokie ryzyko uszkodzenia skóry.", recommendation.description)
        assertTrue(recommendation.recommendation.contains("Unikaj przebywania na słońcu"))
    }

    @Test
    fun `getRecommendation returns correct info for extreme UV index`() {
        // Given
        val uvIndex = 11.5

        // When
        val recommendation = UVRecommendation.getRecommendation(uvIndex)

        // Then
        assertEquals("Ekstremalny", recommendation.level)
        assertEquals("Ekstremalne ryzyko uszkodzenia skóry.", recommendation.description)
        assertTrue(recommendation.recommendation.contains("Pozostań w cieniu"))
    }
}