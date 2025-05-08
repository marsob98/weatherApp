package com.example.weatherapp.ui.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.data.remote.model.UVIndexResponse
import com.example.weatherapp.ui.theme.WeatherAppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UVIndexCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun uvIndexCard_displaysCorrectData() {
        // Given
        val uvIndex = UVIndexResponse(
            lat = 51.1079,
            lon = 17.0385,
            dateIso = "2023-05-20",
            date = 1621512000L,
            value = 7.2
        )

        // When
        composeTestRule.setContent {
            WeatherAppTheme {
                UVIndexCard(uvIndex = uvIndex)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Indeks UV").assertExists()
        composeTestRule.onNodeWithText("7").assertExists() // rounded value
        composeTestRule.onNodeWithText("Wysoki").assertExists()
        composeTestRule.onNodeWithText("Wysokie ryzyko uszkodzenia skóry z powodu UV.").assertExists()
        composeTestRule.onNodeWithText("Zalecenia:").assertExists()
    }

    @Test
    fun uvIndexCard_notDisplayedWhenNull() {
        // Given
        val uvIndex = null

        // When
        composeTestRule.setContent {
            WeatherAppTheme {
                UVIndexCard(uvIndex = uvIndex)
            }
        }

        // Then
        composeTestRule.onNodeWithText("Indeks UV").assertDoesNotExist()
    }
}