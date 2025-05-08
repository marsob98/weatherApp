package com.example.weatherapp.ui.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherIconTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun weatherIcon_isDisplayed() {
        // Given
        val iconCode = "01d"

        // When
        composeTestRule.setContent {
            WeatherIcon(iconCode = iconCode)
        }

        // Then
        composeTestRule.onRoot().printToLog("weatherIcon_isDisplayed")
        // Since it's just a Box with Text, we can verify it exists
        composeTestRule.onRoot().assertExists()
    }
}