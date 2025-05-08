package com.example.weatherapp.ui.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.data.remote.model.*
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.viewmodel.FavouriteViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
class CurrentWeatherCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun currentWeatherCard_displaysCorrectData() {
        // Given
        val weather = createMockWeatherResponse()
        val favouriteViewModel = mock(FavouriteViewModel::class.java)

        // When
        composeTestRule.setContent {
            WeatherAppTheme {
                CurrentWeatherCard(
                    weather = weather,
                    favouriteViewModel = favouriteViewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Wrocław").assertExists()
        composeTestRule.onNodeWithText("22°").assertExists()
        composeTestRule.onNodeWithText("clear sky").assertExists()
    }

    // Pomocnicza metoda do tworzenia odpowiedzi mockowanej
    private fun createMockWeatherResponse(): WeatherResponse {
        return WeatherResponse(
            Coord(17.0385, 51.1079),
            listOf(Weather(800, "Clear", "clear sky", "01d")),
            "stations",
            Main(22.5, 20.0, 19.0, 25.0, 1013, 65),
            10000,
            Wind(5.0, 90),
            Clouds(10),
            1621512000,
            Sys(1, 123, "PL", 1621476800, 1621533600),
            7200,
            3081368,
            "Wrocław",
            200
        )
    }
}