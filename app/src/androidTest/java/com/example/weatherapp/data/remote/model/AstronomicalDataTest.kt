package com.example.weatherapp.data.remote.model

import com.example.weatherapp.util.MockLog
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AstronomicalDataTest {

    @Before
    fun setup() {
        // Mockowanie klasy Log
        MockLog.mock()
    }

    @Test
    fun getDayLength_calculatesCorrectly() {
        // Given
        val sunrise = 1621476800L // 2021-05-20 06:00:00 UTC
        val sunset = 1621533600L  // 2021-05-20 22:00:00 UTC
        val astronomicalData = AstronomicalData(sunrise, sunset)

        // When
        val dayLength = astronomicalData.getDayLength()

        // Then
        assertEquals("16:00", dayLength)
    }

    @Test
    fun getDayProgress_calculatesCorrectly_duringDaytime() {
        // Given
        val sunrise = 1621476800L // 2021-05-20 06:00:00 UTC
        val sunset = 1621533600L  // 2021-05-20 22:00:00 UTC
        val currentTime = 1621505400L // 2021-05-20 14:00:00 UTC
        val astronomicalData = AstronomicalData(sunrise, sunset)

        // When
        val progress = astronomicalData.getDayProgress(currentTime)

        // Then
        assertEquals(50, progress)
    }

    @Test
    fun getDayProgress_returns0_beforeSunrise() {
        // Given
        val sunrise = 1621476800L // 2021-05-20 06:00:00 UTC
        val sunset = 1621533600L  // 2021-05-20 22:00:00 UTC
        val currentTime = 1621472400L // 2021-05-20 04:00:00 UTC
        val astronomicalData = AstronomicalData(sunrise, sunset)

        // When
        val progress = astronomicalData.getDayProgress(currentTime)

        // Then
        assertEquals(0, progress)
    }

    @Test
    fun getDayProgress_returns100_afterSunset() {
        // Given
        val sunrise = 1621476800L // 2021-05-20 06:00:00 UTC
        val sunset = 1621533600L  // 2021-05-20 22:00:00 UTC
        val currentTime = 1621537200L // 2021-05-20 23:00:00 UTC
        val astronomicalData = AstronomicalData(sunrise, sunset)

        // When
        val progress = astronomicalData.getDayProgress(currentTime)

        // Then
        assertEquals(100, progress)
    }

    @Test
    fun fromWeatherResponse_createsCorrectAstronomicalData() {
        // Given
        val weatherResponse = WeatherResponse(
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

        // When
        val astronomicalData = AstronomicalData.fromWeatherResponse(weatherResponse)

        // Then
        assertEquals(1621476800L, astronomicalData.sunrise)
        assertEquals(1621533600L, astronomicalData.sunset)
        assertEquals(7200, astronomicalData.timezone)
    }
}