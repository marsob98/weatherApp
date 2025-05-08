package com.example.weatherapp.viewmodel

import android.content.Context
import android.location.Location
import com.example.weatherapp.R
import com.example.weatherapp.data.remote.model.*
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.utils.LocationManager
import com.example.weatherapp.utils.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
import org.mockito.kotlin.any

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class WeatherViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var repository: WeatherRepository

    @Mock
    private lateinit var locationManager: LocationManager

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var location: Location

    private lateinit var viewModel: WeatherViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // Mockowanie NetworkUtils
        mockStatic(NetworkUtils::class.java).use { mockedNetworkUtils ->
            mockedNetworkUtils.`when`<Boolean> {
                NetworkUtils.isNetworkAvailable(context)
            }.thenReturn(true)
        }

        // Mockowanie Stringów z zasobów
        `when`(context.getString(R.string.error_network)).thenReturn("Network error")
        `when`(context.getString(R.string.error_no_location_data)).thenReturn("No location data")
        `when`(context.getString(R.string.error_generic, any())).thenReturn("Generic error")
        `when`(context.getString(R.string.error_loading_weather)).thenReturn("Error loading weather")
        `when`(context.getString(R.string.error_location, any())).thenReturn("Location error")
        `when`(context.getString(R.string.error_search, any())).thenReturn("Search error")

        // Mockowanie LocationManager
        `when`(locationManager.hasLocationPermission()).thenReturn(true)
        `when`(locationManager.getLastLocation()).thenReturn(location)
        `when`(locationManager.locationUpdates()).thenReturn(flowOf(location))
        `when`(location.latitude).thenReturn(51.1079)
        `when`(location.longitude).thenReturn(17.0385)

        // Mockowanie odpowiedzi z repozytorium
        val weatherResponse = createMockWeatherResponse()
        val forecastResponse = createMockForecastResponse()
        `when`(repository.getCurrentWeather(anyString())).thenReturn(weatherResponse)
        `when`(repository.getForecast(anyString())).thenReturn(forecastResponse)
        `when`(repository.getCurrentWeatherByCoordinates(anyDouble(), anyDouble())).thenReturn(weatherResponse)
        `when`(repository.getForecastByCoordinates(anyDouble(), anyDouble())).thenReturn(forecastResponse)

        // Inicjalizacja ViewModel
        viewModel = WeatherViewModel(repository, locationManager, context)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `init loads weather data and forecast`() = testDispatcher.runBlockingTest {
        // Given - już ustawione w setup()

        // When - ViewModel już zainicjalizowany w setup()

        // Then
        assertNotNull(viewModel.currentWeatherState.value)
        assertNotNull(viewModel.forecastState.value)
        assertEquals("Wrocław", viewModel.currentWeatherState.value?.name)
    }

    @Test
    fun `getWeatherForCity loads correct data`() = testDispatcher.runBlockingTest {
        // Given
        val cityName = "Kraków"

        // When
        viewModel.getWeatherForCity(cityName)

        // Then
        verify(repository).getCurrentWeather(cityName)
        verify(repository).getForecast(cityName)
        assertNotNull(viewModel.currentWeatherState.value)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `getWeatherForCurrentLocation calls appropriate methods`() = testDispatcher.runBlockingTest {
        // Given
        val lat = 51.1079
        val lon = 17.0385

        // When
        viewModel.getWeatherForCurrentLocation()

        // Then
        verify(locationManager).getLastLocation()
        verify(repository).getCurrentWeatherByCoordinates(lat, lon)
        verify(repository).getForecastByCoordinates(lat, lon)
        assertNotNull(viewModel.locationWeatherState.value)
        assertFalse(viewModel.isLocationLoading.value)
    }

    @Test
    fun `searchCities returns search results`() = testDispatcher.runBlockingTest {
        // Given
        val query = "Wroc"
        val searchResults = listOf(createMockGeocodingResponse())
        `when`(repository.searchCity(query)).thenReturn(searchResults)

        // When
        viewModel.searchCities(query)

        // Then
        verify(repository).searchCity(query)
        assertEquals(searchResults, viewModel.searchResults.value)
        assertFalse(viewModel.isSearching.value)
    }

    @Test
    fun `searchCities ignores short queries`() = testDispatcher.runBlockingTest {
        // Given
        val query = "Wr"  // Mniej niż 3 znaki

        // When
        viewModel.searchCities(query)

        // Then
        verify(repository, never()).searchCity(any())
        assertTrue(viewModel.searchResults.value.isEmpty())
    }

    @Test
    fun `getDayData retrieves data for specified day`() = testDispatcher.runBlockingTest {
        // Given
        val timestamp = 1621512000L // 2021-05-20
        val forecastResponse = createMockForecastResponse()
        viewModel.getWeatherForCity("Wrocław") // Ustawiamy forecastState

        // Ustawiamy mocki dla dodatkowych danych
        val uvResponse = UVIndexResponse(51.1079, 17.0385, "2021-05-20", 1621512000L, 5.2)
        val airQualityResponse = createMockAirQualityResponse()
        val alertResponse = createMockAlertResponse()

        `when`(repository.getCurrentUVIndex(anyDouble(), anyDouble())).thenReturn(uvResponse)
        `when`(repository.getCurrentAirQuality(anyDouble(), anyDouble())).thenReturn(airQualityResponse)
        `when`(repository.getWeatherAlerts(anyDouble(), anyDouble())).thenReturn(alertResponse)

        // When
        viewModel.getDayData(timestamp)

        // Then
        verify(repository).getCurrentUVIndex(anyDouble(), anyDouble())
        verify(repository).getCurrentAirQuality(anyDouble(), anyDouble())
        verify(repository).getWeatherAlerts(anyDouble(), anyDouble())

        assertNotNull(viewModel.uvIndexState.value)
        assertEquals(5.2, viewModel.uvIndexState.value?.value, 0.01)
    }

    // Pomocnicze metody do tworzenia odpowiedzi mockowanych
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

    private fun createMockGeocodingResponse(): GeocodingResponse {
        return GeocodingResponse(
            "Wrocław",
            mapOf("pl" to "Wrocław", "en" to "Wroclaw"),
            51.1079,
            17.0385,
            "PL",
            "Lower Silesia"
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