package com.example.weatherapp.data.remote.model

import org.junit.Assert.*
import org.junit.Test

class PrecipitationInfoTest {

    @Test
    fun `fromRainData creates correct PrecipitationInfo for light rain`() {
        // Given
        val rainData = RainData(0.3, null)

        // When
        val precipInfo = PrecipitationInfo.fromRainData(rainData)

        // Then
        assertNotNull(precipInfo)
        assertEquals(0.3, precipInfo!!.amount, 0.01)
        assertEquals("Lekkie opady", precipInfo.intensity)
        assertEquals("Deszcz", precipInfo.type)
        assertEquals("1h", precipInfo.period)
    }

    @Test
    fun `fromRainData creates correct PrecipitationInfo for moderate rain`() {
        // Given
        val rainData = RainData(null, 3.5)

        // When
        val precipInfo = PrecipitationInfo.fromRainData(rainData)

        // Then
        assertNotNull(precipInfo)
        assertEquals(3.5, precipInfo!!.amount, 0.01)
        assertEquals("Umiarkowane opady", precipInfo.intensity)
        assertEquals("Deszcz", precipInfo.type)
        assertEquals("3h", precipInfo.period)
    }

    @Test
    fun `fromRainData creates correct PrecipitationInfo for heavy rain`() {
        // Given
        val rainData = RainData(6.0, null)

        // When
        val precipInfo = PrecipitationInfo.fromRainData(rainData)

        // Then
        assertNotNull(precipInfo)
        assertEquals(6.0, precipInfo!!.amount, 0.01)
        assertEquals("Intensywne opady", precipInfo.intensity)
        assertEquals("Deszcz", precipInfo.type)
        assertEquals("1h", precipInfo.period)
    }

    @Test
    fun `fromRainData returns null for null data`() {
        // Given
        val rainData = null

        // When
        val precipInfo = PrecipitationInfo.fromRainData(rainData)

        // Then
        assertNull(precipInfo)
    }

    @Test
    fun `fromRainData returns null for empty data`() {
        // Given
        val rainData = RainData(null, null)

        // When
        val precipInfo = PrecipitationInfo.fromRainData(rainData)

        // Then
        assertNull(precipInfo)
    }

    @Test
    fun `fromSnowData creates correct PrecipitationInfo for light snow`() {
        // Given
        val snowData = SnowData(0.4, null)

        // When
        val precipInfo = PrecipitationInfo.fromSnowData(snowData)

        // Then
        assertNotNull(precipInfo)
        assertEquals(0.4, precipInfo!!.amount, 0.01)
        assertEquals("Lekkie opady", precipInfo.intensity)
        assertEquals("Śnieg", precipInfo.type)
        assertEquals("1h", precipInfo.period)
    }
}