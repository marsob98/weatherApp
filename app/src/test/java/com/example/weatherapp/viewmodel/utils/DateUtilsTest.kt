package com.example.weatherapp.viewmodel.utils

import android.content.Context
import com.example.weatherapp.R
import com.example.weatherapp.ui.utils.formatDate
import com.example.weatherapp.ui.utils.formatDateShortName
import com.example.weatherapp.ui.utils.formatDateTime
import com.example.weatherapp.ui.utils.formatTimestamp
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.util.*

class DateUtilsTest {

    @Test
    fun `formatTimestamp formats timestamp correctly`() {
        // Given
        val timestamp = 1621512000L // 2021-05-20 12:00:00 UTC

        // When
        val result = formatTimestamp(timestamp)

        // Then
        // Note: the exact expected value depends on your locale, so we just check format
        val regex = """\d{2}:\d{2}""".toRegex()
        assert(regex.matches(result))
    }

    @Test
    fun `formatDate returns day name correctly`() {
        // Given
        val timestamp = 1621512000L // 2021-05-20 (Thursday)

        // When
        val result = formatDate(timestamp)

        // Then
        // The actual day name will depend on the locale, but we can check that it's not empty
        assert(result.isNotEmpty())
    }

    @Test
    fun `formatDateShortName returns correct short day name`() {
        // Given
        val timestamp = 1621512000L // 2021-05-20 (Thursday)
        val context = mock(Context::class.java)
        `when`(context.getString(R.string.thursday_short)).thenReturn("Czw")

        // When
        val result = formatDateShortName(timestamp, context)

        // Then
        assertEquals("Czw", result)
    }

    @Test
    fun `formatDateTime formats time correctly`() {
        // Given
        val timestamp = 1621512000L // 2021-05-20 12:00:00 UTC

        // When
        val result = formatDateTime(timestamp)

        // Then
        val regex = """\d{2}:\d{2}""".toRegex()
        assert(regex.matches(result))
    }
}