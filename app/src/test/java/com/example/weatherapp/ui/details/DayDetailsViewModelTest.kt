package com.example.weatherapp.ui.details

import com.example.weatherapp.data.remote.model.*
import com.example.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DayDetailsViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var repository: WeatherRepository

    private lateinit var viewModel: DayDetailsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = DayDetailsViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `filterForecastForDay filters correctly`() = testDispatcher.runBlockingTest {
        // Given
        val allForecast = listOf(
            createForecastItem(1621512000), // 2021-05-20 12:00:00
            createForecastItem(1621522800), // 2021-05-20 15:00:00
            createForecastItem(1621598400)  // 2021-05-21 12:00:00
        )
        val day1Timestamp = 1621512000L // 2021-05-20

        // When
        viewModel.filterForecastForDay(allForecast, day1Timestamp)

        // Then
        assertEquals(2, viewModel.dayForecast.value.size)
        assertEquals(1621512000L, viewModel.dayForecast.value[0].dt)
        assertEquals(1621522800L, viewModel.dayForecast.value[1].dt)
    }

    @Test
    fun `loadDayDetails by coordinates loads all data`() = testDispatcher.runBlockingTest {
        // Given
        val lat = 51.1079
        val lon = 17.0385

        // Mockowanie odpowiedzi z repozytorium
        val uvResponse = UVIndexResponse(lat, lon, "2021-05-20", 1621512000L, 5.2)
        val airQualityResponse = createMockAirQualityResponse()
        val alertResponse = createMockAlertResponse()

        `when`(repository.getCurrentUVIndex(lat, lon)).thenReturn(uvResponse)
        `when`(repository.getCurrentAirQuality(lat, lon)).thenReturn(airQualityResponse)
        `when`(repository.getWeatherAlerts(lat, lon)).thenReturn(alertResponse)

        // When
        viewModel.loadDayDetails(null, lat, lon)

        // Then
        verify(repository).getCurrentUVIndex(lat, lon)
        verify(repository).getCurrentAirQuality(lat, lon)
        verify(repository).getWeatherAlerts(lat, lon)

        assertEquals(uvResponse, viewModel.uvIndex.value)
        assertEquals(airQualityResponse, viewModel.airQuality.value)
        assertEquals(alertResponse.alerts, viewModel.alerts.value)
        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `loadDayDetails by city loads all data`() = testDispatcher.runBlockingTest {
        // Given
        val cityName = "Wrocław"
        val lat = 51.1079
        val lon = 17.0385

        // Mockowanie odpowiedzi z repozytorium
        val weatherResponse = createMockWeatherResponse()
        val uvResponse = UVIndexResponse(lat, lon, "2021-05-20", 1621512000L, 5.2)
        val airQualityResponse = createMockAirQualityResponse()
        val alertResponse = createMockAlertResponse()

        `when`(repository.getCurrentWeather(cityName)).thenReturn(weatherResponse)
        `when`(repository.getCurrentUVIndex(lat, lon)).thenReturn(uvResponse)
        `when`(repository.getCurrentAirQuality(lat, lon)).thenReturn(airQualityResponse)
        `when`(repository.getWeatherAlerts(lat, lon)).thenReturn(alertResponse)

        // When
        viewModel.loadDayDetails(cityName, null, null)

        // Then
        verify(repository).getCurrentWeather(cityName)
        verify(repository).getCurrentUVIndex(lat, lon)
        verify(repository).getCurrentAirQuality(lat, lon)
        verify(repository).getWeatherAlerts(lat, lon)

        assertEquals(uvResponse, viewModel.uvIndex.value)
        assertEquals(airQualityResponse, viewModel.airQuality.value)
        assertEquals(alertResponse.alerts, viewModel.alerts.value)
        assertNotNull(viewModel.astronomicalData.value)
        assertFalse(viewModel.isLoading.value)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `loadDayDetails handles error correctly`() = testDispatcher.runBlockingTest {
        // Given
        val cityName = "Wrocław"
        val errorMessage = "Network error"
        `when`(repository.getCurrentWeather(cityName)).thenThrow(RuntimeException(errorMessage))

        // When
        viewModel.loadDayDetails(cityName, null, null)

        // Then
        assertEquals(errorMessage, viewModel.error.value)
        assertFalse(viewModel.isLoading.value)
    }

    // Pomocnicze metody do tworzenia odpowiedzi mockowanych
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

    private fun createMockAirQualityResponse(): AirQualityResponse {
        val components = AirQualityComponents(
            300.0, 10.0, 20.0, 30.0, 5.0, 15.0, 30.0, 0.5
        )
        val aqiMain = AirQualityMain(2)
        val aqiData = AirQualityData(aqiMain, components, 1621512000L)

        return AirQualityResponse(
            Coord(17.0385, 51.1079),
            listOf(aqiData)
        )
    }

    private fun createMockAlertResponse(): AlertResponse {
        val alert = Alert(
            "Test Sender",
            "Wind Warning",
            1621512000L,
            1621533600L,
            "Strong wind warning",
            listOf("Wind")
        )

        return AlertResponse(
            51.1079,
            17.0385,
            "Europe/Warsaw",
            7200,
            listOf(alert),
            null,
            null
        )
    }
}