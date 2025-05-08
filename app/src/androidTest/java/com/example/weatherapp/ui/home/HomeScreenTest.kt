package com.example.weatherapp.ui.home

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.data.remote.model.*
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.viewmodel.WeatherViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen_displaysWeatherData() {
        // Given
        val weatherViewModel = mock(WeatherViewModel::class.java)

        // Mock states
        `when`(weatherViewModel.currentWeatherState).thenReturn(mutableStateOf(createMockWeatherResponse()))
        `when`(weatherViewModel.forecastState).thenReturn(mutableStateOf(createMockForecastResponse()))
        `when`(weatherViewModel.isLoading).thenReturn(mutableStateOf(false))
        `when`(weatherViewModel.error).thenReturn(mutableStateOf(null))
        `when`(weatherViewModel.locationPermissionGranted).thenReturn(mutableStateOf(true))
        `when`(weatherViewModel.locationWeatherState).thenReturn(mutableStateOf(createMockWeatherResponse()))
        `when`(weatherViewModel.isLocationLoading).thenReturn(mutableStateOf(false))
        `when`(weatherViewModel.uvIndexState).thenReturn(mutableStateOf(null))
        `when`(weatherViewModel.airQualityState).thenReturn(mutableStateOf(null))
        `when`(weatherViewModel.alertsState).thenReturn(mutableStateOf(null))
        `when`(weatherViewModel.astronomicalData).thenReturn(mutableStateOf(null))
        `when`(weatherViewModel.precipitationData).thenReturn(mutableStateOf(null))

        val navController = mock(NavController::class.java)

        // When
        composeTestRule.setContent {
            WeatherAppTheme {
                HomeScreen(
                    weatherViewModel = weatherViewModel,
                    navController = navController,
                    onNavigateToSearch = {},
                    onNavigateToFavorites = {},
                    onNavigateToDayDetails = {}
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Wrocław").assertExists()
        composeTestRule.onNodeWithText("22°").assertExists()
        composeTestRule.onNodeWithText("Prognoza godzinowa").assertExists()
        composeTestRule.onNodeWithText("Prognoza tygodniowa").assertExists()
        composeTestRule.onNodeWithText("Szczegóły").assertExists()
    }

    @Test
    fun homeScreen_displaysLoadingIndicator() {
        // Given
        val weatherViewModel = mock(WeatherViewModel::class.java)

        // Mock states
        `when`(weatherViewModel.currentWeatherState).thenReturn(mutableStateOf(null))
        `when`(weatherViewModel.forecastState).thenReturn(mutableStateOf(null))
        `when`(weatherViewModel.isLoading).thenReturn(mutableStateOf(true))
        `when`(weatherViewModel.error).thenReturn(mutableStateOf(null))
        `when`(weatherViewModel.locationPermissionGranted).thenReturn(mutableStateOf(true))
        `when`(weatherViewModel.locationWeatherState).thenReturn(mutableStateOf(null))
        `when`(weatherViewModel.isLocationLoading).thenReturn(mutableStateOf(false))

        val navController = mock(NavController::class.java)

        // When
        composeTestRule.setContent {
            WeatherAppTheme {
                HomeScreen(
                    weatherViewModel = weatherViewModel,
                    navController = navController,
                    onNavigateToSearch = {},
                    onNavigateToFavorites = {},
                    onNavigateToDayDetails = {}
                )
            }
        }

        // Then
        // Test for CircularProgressIndicator
        composeTestRule.onNode(hasProgressBarRangeInfo()).assertExists()
    }

    @Test
    fun homeScreen_displaysErrorMessage() {
        // Given
        val weatherViewModel = mock(WeatherViewModel::class.java)
        val errorMessage = "Błąd sieciowy"

        // Mock states
        `when`(weatherViewModel.currentWeatherState).thenReturn(mutableStateOf(null))
        `when`(weatherViewModel.forecastState).thenReturn(mutableStateOf(null))
        `when`(weatherViewModel.isLoading).thenReturn(mutableStateOf(false))
        `when`(weatherViewModel.error).thenReturn(mutableStateOf(errorMessage))
        `when`(weatherViewModel.locationPermissionGranted).thenReturn(mutableStateOf(true))
        `when`(weatherViewModel.locationWeatherState).thenReturn(mutableStateOf(null))
        `when`(weatherViewModel.isLocationLoading).thenReturn(mutableStateOf(false))

        val navController = mock(NavController::class.java)

        // When
        composeTestRule.setContent {
            WeatherAppTheme {
                HomeScreen(
                    weatherViewModel = weatherViewModel,
                    navController = navController,
                    onNavigateToSearch = {},
                    onNavigateToFavorites = {},
                    onNavigateToDayDetails = {}
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Wystąpił błąd: $errorMessage").assertExists()
    }

    // Helpers
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

    private fun createMockForecastResponse(): ForecastResponse {
        return ForecastResponse(
            listOf(
                ForecastItem(
                    1621512000,
                    Main(22.5, 20.0, 19.0, 25.0, 1013, 65),
                    listOf(Weather(800, "Clear", "clear sky", "01d")),
                    Clouds(10),
                    Wind(5.0, 90),
                    10000,
                    0.1,
                    "2023-05-20 12:00:00"
                )
            ),
            City(
                3081368,
                "Wrocław",
                Coord(17.0385, 51.1079),
                "PL",
                634893,
                7200,
                1621476800,
                1621533600
            )
        )
    }
}