package com.example.weatherapp.data.remote.model

import org.junit.Assert.*
import org.junit.Test

class AirQualityInfoTest {

    @Test
    fun `getAirQualityInfo returns correct info for level 1`() {
        // Given
        val aqi = 1

        // When
        val info = AirQualityInfo.getAirQualityInfo(aqi)

        // Then
        assertEquals("Dobra", info.level)
        assertEquals("Jakość powietrza jest dobra.", info.description)
        assertEquals(0xFF4CAF50, info.color)
        assertEquals("Idealne warunki do aktywności na zewnątrz.", info.recommendation)
    }

    @Test
    fun `getAirQualityInfo returns correct info for level 3`() {
        // Given
        val aqi = 3

        // When
        val info = AirQualityInfo.getAirQualityInfo(aqi)

        // Then
        assertEquals("Niezdrowa dla wrażliwych", info.level)
        assertEquals("Jakość powietrza jest niezdrowa dla osób wrażliwych.", info.description)
        assertEquals(0xFFFF9800, info.color)
        assertTrue(info.recommendation.contains("Osoby starsze, dzieci"))
    }

    @Test
    fun `getAirQualityInfo returns correct info for level 5`() {
        // Given
        val aqi = 5

        // When
        val info = AirQualityInfo.getAirQualityInfo(aqi)

        // Then
        assertEquals("Bardzo niezdrowa", info.level)
        assertEquals("Jakość powietrza jest bardzo niezdrowa.", info.description)
        assertEquals(0xFF9C27B0, info.color)
        assertTrue(info.recommendation.contains("Wszyscy powinni unikać"))
    }

    @Test
    fun `getAirQualityInfo handles invalid values`() {
        // Given
        val aqi = 10 // Nieprawidłowa wartość

        // When
        val info = AirQualityInfo.getAirQualityInfo(aqi)

        // Then
        assertEquals("Niebezpieczna", info.level)
        assertEquals("Jakość powietrza jest niebezpieczna.", info.description)
        assertEquals(0xFF5D4037, info.color)
        assertTrue(info.recommendation.contains("Wszyscy powinni unikać"))
    }
}