package com.example.weatherapp.ui.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.data.remote.model.PrecipitationInfo
import com.example.weatherapp.ui.theme.WeatherAppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PrecipitationCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun precipitationCard_displaysCorrectData() {
        // Given
        val precipInfo = PrecipitationInfo(
            amount = 3.5,
            intensity = "Umiarkowane opady",
            type = "Deszcz",
            period = "1h"
        )

        // When
        composeTestRule.setContent {
            WeatherAppTheme {
                PrecipitationCard(precipitationInfo = precipInfo)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Informacje o opadach").assertExists()
        composeTestRule.onNodeWithText("Deszcz").assertExists()
        composeTestRule.onNodeWithText("Umiarkowane opady").assertExists()
        composeTestRule.onNodeWithText("3.5 mm/1h").assertExists()
        composeTestRule.onNodeWithText("Intensywność").assertExists()
        composeTestRule.onNodeWithText("Zalecenia:").assertExists()
    }

    @Test
    fun precipitationCard_notDisplayedWhenNull() {
        // Given
        val precipInfo = null

        // When
        composeTestRule.setContent {
            WeatherAppTheme {
                PrecipitationCard(precipitationInfo = precipInfo)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Informacje o opadach").assertDoesNotExist()
    }
}