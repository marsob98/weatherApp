package com.example.weatherapp.data.remote.model

import org.junit.Assert.*
import org.junit.Test

class AlertTest {

    @Test
    fun `getAlertSeverity returns WARNING for warning event`() {
        // Given
        val alert = Alert(
            senderName = "Test Sender",
            event = "Wind Warning",
            start = 1621512000,
            end = 1621533600,
            description = "Strong wind warning",
            tags = listOf("Wind")
        )

        // When
        val severity = alert.getAlertSeverity()

        // Then
        assertEquals(AlertSeverity.WARNING, severity)
        assertEquals(0xFFF44336, severity.color) // Czerwony
    }

    @Test
    fun `getAlertSeverity returns WATCH for watch event`() {
        // Given
        val alert = Alert(
            senderName = "Test Sender",
            event = "Thunderstorm Watch",
            start = 1621512000,
            end = 1621533600,
            description = "Possible thunderstorms",
            tags = listOf("Thunderstorm")
        )

        // When
        val severity = alert.getAlertSeverity()

        // Then
        assertEquals(AlertSeverity.WATCH, severity)
        assertEquals(0xFFFF9800, severity.color) // Pomarańczowy
    }

    @Test
    fun `getAlertSeverity returns ADVISORY for advisory event`() {
        // Given
        val alert = Alert(
            senderName = "Test Sender",
            event = "Heat Advisory",
            start = 1621512000,
            end = 1621533600,
            description = "High temperatures expected",
            tags = listOf("Heat")
        )

        // When
        val severity = alert.getAlertSeverity()

        // Then
        assertEquals(AlertSeverity.ADVISORY, severity)
        assertEquals(0xFFFFEB3B, severity.color) // Żółty
    }

    @Test
    fun `getAlertSeverity returns INFORMATION for other events`() {
        // Given
        val alert = Alert(
            senderName = "Test Sender",
            event = "Weather Information",
            start = 1621512000,
            end = 1621533600,
            description = "General weather information",
            tags = listOf("Info")
        )

        // When
        val severity = alert.getAlertSeverity()

        // Then
        assertEquals(AlertSeverity.INFORMATION, severity)
        assertEquals(0xFF2196F3, severity.color) // Niebieski
    }
}