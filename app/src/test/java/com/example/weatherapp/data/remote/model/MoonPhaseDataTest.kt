package com.example.weatherapp.data.remote.model

import org.junit.Assert.*
import org.junit.Test

class MoonPhaseDataTest {

    @Test
    fun `getMoonPhaseName returns correct name for new moon`() {
        // Given
        val moonPhase = MoonPhaseData(0.0, 1621512000L)

        // When
        val phaseName = moonPhase.getMoonPhaseName()

        // Then
        assertEquals("Nów", phaseName)
    }

    @Test
    fun `getMoonPhaseName returns correct name for waxing crescent`() {
        // Given
        val moonPhase = MoonPhaseData(0.1, 1621512000L)

        // When
        val phaseName = moonPhase.getMoonPhaseName()

        // Then
        assertEquals("Sierp przybywający", phaseName)
    }

    @Test
    fun `getMoonPhaseName returns correct name for first quarter`() {
        // Given
        val moonPhase = MoonPhaseData(0.25, 1621512000L)

        // When
        val phaseName = moonPhase.getMoonPhaseName()

        // Then
        assertEquals("Pierwsza kwadra", phaseName)
    }

    @Test
    fun `getMoonPhaseName returns correct name for waxing gibbous`() {
        // Given
        val moonPhase = MoonPhaseData(0.4, 1621512000L)

        // When
        val phaseName = moonPhase.getMoonPhaseName()

        // Then
        assertEquals("Przybywający księżyc garbaty", phaseName)
    }

    @Test
    fun `getMoonPhaseName returns correct name for full moon`() {
        // Given
        val moonPhase = MoonPhaseData(0.5, 1621512000L)

        // When
        val phaseName = moonPhase.getMoonPhaseName()

        // Then
        assertEquals("Pełnia", phaseName)
    }

    @Test
    fun `getMoonPhaseEmoji returns correct emoji for new moon`() {
        // Given
        val moonPhase = MoonPhaseData(0.0, 1621512000L)

        // When
        val emoji = moonPhase.getMoonPhaseEmoji()

        // Then
        assertEquals("🌑", emoji)
    }

    @Test
    fun `getMoonPhaseEmoji returns correct emoji for full moon`() {
        // Given
        val moonPhase = MoonPhaseData(0.5, 1621512000L)

        // When
        val emoji = moonPhase.getMoonPhaseEmoji()

        // Then
        assertEquals("🌕", emoji)
    }
}