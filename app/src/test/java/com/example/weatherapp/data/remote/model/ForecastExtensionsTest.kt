package com.example.weatherapp.data.remote.model

import org.junit.Assert.*
import org.junit.Test
import java.util.*

class ForecastExtensionsTest {

    private val forecasts = listOf(
        createForecastItem(1621512000, 20.0, 70, 5.0, "Clear"),  // 2021-05-20 12:00:00
        createForecastItem(1621522800, 22.0, 65, 4.5, "Clear"),  // 2021-05-20 15:00:00
        createForecastItem(1621533600, 18.0, 75, 3.5, "Clouds"), // 2021-05-20 18:00:00
        createForecastItem(1621544400, 15.0, 80, 3.0, "Rain"),   // 2021-05-20 21:00:00
        createForecastItem(1621598400, 17.0, 78, 4.0, "Clouds")  // 2021-05-21 12:00:00
    )

    @Test
    fun `filterForDay returns correct forecasts`() {
        // Given
        val day1Timestamp = 1621512000L // 2021-05-20

        // When
        val day1Forecasts = forecasts.filterForDay(day1Timestamp)

        // Then
        assertEquals(4, day1Forecasts.size)
        // Check that all forecasts are from the same day
        val calendar = Calendar.getInstance()
        val day = Calendar.getInstance().apply {
            timeInMillis = day1Timestamp * 1000
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.get(Calendar.DAY_OF_YEAR)

        day1Forecasts.forEach {
            calendar.timeInMillis = it.dt * 1000
            assertEquals(day, calendar.get(Calendar.DAY_OF_YEAR))
        }
    }

    @Test
    fun `getAverageTemperature calculates correctly`() {
        // Given
        val day1Forecasts = forecasts.filterForDay(1621512000L)

        // When
        val averageTemp = day1Forecasts.getAverageTemperature()

        // Then
        assertEquals(18.75, averageTemp, 0.01)
    }

    @Test
    fun `getMinTemperature finds minimum correctly`() {
        // Given
        val day1Forecasts = forecasts.filterForDay(1621512000L)

        // When
        val minTemp = day1Forecasts.getMinTemperature()

        // Then
        assertEquals(15.0, minTemp, 0.01)
    }

    @Test
    fun `getMaxTemperature finds maximum correctly`() {
        // Given
        val day1Forecasts = forecasts.filterForDay(1621512000L)

        // When
        val maxTemp = day1Forecasts.getMaxTemperature()

        // Then
        assertEquals(22.0, maxTemp, 0.01)
    }

    @Test
    fun `getAverageHumidity calculates correctly`() {
        // Given
        val day1Forecasts = forecasts.filterForDay(1621512000L)

        // When
        val avgHumidity = day1Forecasts.getAverageHumidity()

        // Then
        assertEquals(72, avgHumidity)
    }

    @Test
    fun `getAverageWindSpeed calculates correctly`() {
        // Given
        val day1Forecasts = forecasts.filterForDay(1621512000L)

        // When
        val avgWindSpeed = day1Forecasts.getAverageWindSpeed()

        // Then
        assertEquals(4.0, avgWindSpeed, 0.01)
    }

    @Test
    fun `getMostFrequentWeather returns most common weather`() {
        // Given
        val day1Forecasts = forecasts.filterForDay(1621512000L)

        // When
        val mostFrequentWeather = day1Forecasts.getMostFrequentWeather()

        // Then
        assertNotNull(mostFrequentWeather)
        assertEquals("Clear", mostFrequentWeather?.main)
    }

    @Test
    fun `hasRain detects rain correctly`() {
        // Given
        val day1Forecasts = forecasts.filterForDay(1621512000L)

        // When
        val hasRain = day1Forecasts.hasRain()

        // Then
        assertTrue(hasRain)
    }

    @Test
    fun `hasSnow returns false when no snow`() {
        // Given
        val day1Forecasts = forecasts.filterForDay(1621512000L)

        // When
        val hasSnow = day1Forecasts.hasSnow()

        // Then
        assertFalse(hasSnow)
    }

    @Test
    fun `getAverageCloudiness calculates correctly`() {
        // Given
        val day1Forecasts = forecasts.filterForDay(1621512000L)

        // When
        val avgCloudiness = day1Forecasts.getAverageCloudiness()

        // Then
        // Obliczenie średniej: (0 + 0 + 50 + 50) / 4 = 25
        assertEquals(25, avgCloudiness)
    }

    @Test
    fun `getDayName returns correct day name`() {
        // Given
        val day1Forecasts = forecasts.filterForDay(1621512000L)

        // When
        val dayName = day1Forecasts.getDayName()

        // Then
        // Actual day name depends on locale, but we check it's not empty
        assertTrue(dayName.isNotEmpty())
    }

    @Test
    fun `createPrecipitationInfo creates correct info for rain`() {
        // Given
        val day1Forecasts = forecasts.filterForDay(1621512000L)

        // When
        val precipInfo = day1Forecasts.createPrecipitationInfo()

        // Then
        assertNotNull(precipInfo)
        assertEquals("Deszcz", precipInfo?.type)
        // Inne właściwości są ustalane w domyślnej implementacji
    }

    // Helper method to create test data
    private fun createForecastItem(
        timestamp: Long,
        temp: Double,
        humidity: Int,
        windSpeed: Double,
        weatherMain: String
    ): ForecastItem {
        return ForecastItem(
            dt = timestamp,
            main = Main(
                temp = temp,
                feelsLike = temp - 2.0,
                tempMin = temp - 3.0,
                tempMax = temp + 3.0,
                pressure = 1013,
                humidity = humidity
            ),
            weather = listOf(
                Weather(
                    id = if (weatherMain == "Clear") 800 else if (weatherMain == "Clouds") 801 else 500,
                    main = weatherMain,
                    description = "$weatherMain sky",
                    icon = if (weatherMain == "Clear") "01d" else if (weatherMain == "Clouds") "02d" else "10d"
                )
            ),
            clouds = Clouds(if (weatherMain == "Clear") 0 else 50),
            wind = Wind(speed = windSpeed, deg = 90),
            visibility = 10000,
            pop = if (weatherMain == "Rain") 0.7 else 0.0,
            dtTxt = "2023-05-20 12:00:00"
        )
    }
}