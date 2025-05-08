package com.example.weatherapp.ui.search

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.data.remote.model.GeocodingResponse
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.viewmodel.WeatherViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
class SearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun searchScreen_displaysSearchField() {
        // Given
        val weatherViewModel = mock(WeatherViewModel::class.java)
        `when`(weatherViewModel.searchResults).thenReturn(MutableStateFlow(emptyList()))
        `when`(weatherViewModel.isSearching).thenReturn(mutableStateOf(false))
        `when`(weatherViewModel.error).thenReturn(mutableStateOf(null))

        // When
        composeTestRule.setContent {
            WeatherAppTheme {
                SearchScreen(
                    weatherViewModel = weatherViewModel,
                    onNavigateBack = {}
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Wyszukaj miasto").assertExists()
        composeTestRule.onNode(hasSetTextAction()).assertExists()
    }

    @Test
    fun searchScreen_displaysResults() {
        // Given
        val weatherViewModel = mock(WeatherViewModel::class.java)
        val searchResults = listOf(
            GeocodingResponse("Wrocław", null, 51.1079, 17.0385, "PL", "Lower Silesia"),
            GeocodingResponse("Warszawa", null, 52.2297, 21.0122, "PL", "Mazovia")
        )

        `when`(weatherViewModel.searchResults).thenReturn(MutableStateFlow(searchResults))
        `when`(weatherViewModel.isSearching).thenReturn(mutableStateOf(false))
        `when`(weatherViewModel.error).thenReturn(mutableStateOf(null))

        // When
        composeTestRule.setContent {
            WeatherAppTheme {
                SearchScreen(
                    weatherViewModel = weatherViewModel,
                    onNavigateBack = {}
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Wrocław").assertExists()
        composeTestRule.onNodeWithText("Lower Silesia, PL").assertExists()
        composeTestRule.onNodeWithText("Warszawa").assertExists()
        composeTestRule.onNodeWithText("Mazovia, PL").assertExists()
    }

    @Test
    fun searchScreen_displaysLoadingIndicator() {
        // Given
        val weatherViewModel = mock(WeatherViewModel::class.java)
        `when`(weatherViewModel.searchResults).thenReturn(MutableStateFlow(emptyList()))
        `when`(weatherViewModel.isSearching).thenReturn(mutableStateOf(true))
        `when`(weatherViewModel.error).thenReturn(mutableStateOf(null))

        // When
        composeTestRule.setContent {
            WeatherAppTheme {
                SearchScreen(
                    weatherViewModel = weatherViewModel,
                    onNavigateBack = {}
                )
            }
        }

        // Then
        composeTestRule.onNode(hasProgressBarRangeInfo()).assertExists()
    }

    @Test
    fun searchScreen_displayErrorMessage() {
        // Given
        val weatherViewModel = mock(WeatherViewModel::class.java)
        val errorMessage = "Błąd wyszukiwania"

        `when`(weatherViewModel.searchResults).thenReturn(MutableStateFlow(emptyList()))
        `when`(weatherViewModel.isSearching).thenReturn(mutableStateOf(false))
        `when`(weatherViewModel.error).thenReturn(mutableStateOf(errorMessage))

        // When
        composeTestRule.setContent {
            WeatherAppTheme {
                SearchScreen(
                    weatherViewModel = weatherViewModel,
                    onNavigateBack = {}
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText(errorMessage).assertExists()
    }
}