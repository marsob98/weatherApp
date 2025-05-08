package com.example.weatherapp.ui.details

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.data.remote.model.*
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.viewmodel.WeatherViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
class DayDetailsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun dayDetailsScreen_displaysCorrectData() {
        // Given
        val date = 1621512000L // 2021-05-20
        val forecastItems = listOf(
            createForecastItem(1621512000), // 2021-05-20 12:00:00
            createForecastItem(1621522800)  // 2021-05-20 15:00:00
        )

        val weatherViewModel = mock(WeatherViewModel::class.java)
        `when`(weatherViewModel.currentWeatherState).thenReturn(mutableStateOf(createMockWeatherResponse()))
        `when`(weatherViewModel.uvIndexState).thenReturn(mutableStateOf(null))
        `when`(weatherViewModel.airQualityState).thenReturn(mutableStateOf(null))
        `when`(weatherViewModel.alertsState).thenReturn(mutableStateOf(null))
        `when`(weatherViewModel.astronomicalData).thenReturn(mutableStateOf(null))
        `when`(weatherViewModel.precipitationData).thenReturn(mutableStateOf(null))

        // When
        composeTestRule.setContent {
            WeatherAppTheme {
                DayDetailsScreen(
                    date = date,
                    forecastItems = forecastItems,
                    onNavigateBack = {},
                    weatherViewModel = weatherViewModel
                )
            }
        }

        // Then
        // Czwartek powinien być widoczny (dzień tygodnia dla 2021-05-20)
        composeTestRule.onNodeWithText("Czwartek").assertExists()
        composeTestRule.onNodeWithText("Wrocław").assertExists()
        composeTestRule.onNodeWithText("Podsumowanie dnia").assertExists()
        composeTestRule.onNodeWithText("Prognoza godzinowa").assertExists()
    }

    @Test
    fun dayDetailsScreen_displaysNoDataMessage() {
        // Given
        val date = 1621512000L // 2021-05-20
        val forecastItems = emptyList<ForecastItem>()

        val weatherViewModel = mock(WeatherViewModel::class.java)
        `when`(weatherViewModel.currentWeatherState).thenReturn(mutableStateOf(createMockWeatherResponse()))

        // When
        composeTestRule.setContent {
            WeatherAppTheme {
                DayDetailsScreen(
                    date = date,
                    forecastItems = forecastItems,
                    onNavigateBack = {},
                    weatherViewModel = weatherViewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Brak danych dla wybranego dnia").assertExists()
    }

    // Helpers
    private fun createForecastItem(timestamp: Long): ForecastItem {
        return ForecastItem(
            dt = timestamp,
            main = Main(
                temp = 22.5,
                feelsLike = 20.0,
                tempMin = 19.0,
                tempMax = 25.0,
                pressure = 1013,
                humidity = 65
            ),
            weather = listOf(Weather(800, "Clear", "clear sky", "01d")),
            clouds = Clouds(10),
            wind = Wind(5.0, 90),
            visibility = 10000,
            pop = 0.1,
            dtTxt = "2023-05-20 12:00:00"
        )
    }

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