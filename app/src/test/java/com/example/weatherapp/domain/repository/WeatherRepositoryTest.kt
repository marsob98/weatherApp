package com.example.weatherapp.domain.repository

import com.example.weatherapp.data.remote.api.GeocodingApi
import com.example.weatherapp.data.remote.api.WeatherApi
import com.example.weatherapp.data.remote.model.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class WeatherRepositoryTest {

    private lateinit var weatherApi: WeatherApi
    private lateinit var geocodingApi: GeocodingApi
    private lateinit var repository: WeatherRepository
    private val apiKey = "test_api_key"

    @Before
    fun setup() {
        weatherApi = mock(WeatherApi::class.java)
        geocodingApi = mock(GeocodingApi::class.java)
        repository = WeatherRepository(weatherApi, geocodingApi, apiKey)
    }

    @Test
    fun `getCurrentWeather calls api with correct parameters`() = runBlocking {
        // Given
        val cityName = "Wrocław"
        val mockResponse = createMockWeatherResponse()
        `when`(weatherApi.getCurrentWeather(cityName, apiKey)).thenReturn(mockResponse)

        // When
        val result = repository.getCurrentWeather(cityName)

        // Then
        assertEquals(mockResponse, result)
        verify(weatherApi).getCurrentWeather(cityName, apiKey)
    }

    @Test
    fun `getCurrentWeatherByCoordinates calls api with correct parameters`() = runBlocking {
        // Given
        val lat = 51.1079
        val lon = 17.0385
        val mockResponse = createMockWeatherResponse()
        `when`(weatherApi.getCurrentWeatherByCoordinates(lat, lon, apiKey)).thenReturn(mockResponse)

        // When
        val result = repository.getCurrentWeatherByCoordinates(lat, lon)

        // Then
        assertEquals(mockResponse, result)
        verify(weatherApi).getCurrentWeatherByCoordinates(lat, lon, apiKey)
    }

    @Test
    fun `getForecast calls api with correct parameters`() = runBlocking {
        // Given
        val cityName = "Wrocław"
        val mockResponse = createMockForecastResponse()
        `when`(weatherApi.getForecast(cityName, apiKey)).thenReturn(mockResponse)

        // When
        val result = repository.getForecast(cityName)

        // Then
        assertEquals(mockResponse, result)
        verify(weatherApi).getForecast(cityName, apiKey)
    }

    @Test
    fun `searchCity calls api with correct parameters`() = runBlocking {
        // Given
        val query = "Wrocł"
        val limit = 5
        val mockResponse = listOf(createMockGeocodingResponse())
        `when`(geocodingApi.searchCity(query, limit, apiKey)).thenReturn(mockResponse)

        // When
        val result = repository.searchCity(query)

        // Then
        assertEquals(mockResponse, result)
        verify(geocodingApi).searchCity(query, 5, apiKey)
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
}