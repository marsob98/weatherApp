package com.example.weatherapp.domain.repository

import com.example.weatherapp.data.remote.api.GeocodingApi
import com.example.weatherapp.data.remote.api.WeatherApi
import com.example.weatherapp.data.remote.model.*
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val geocodingApi: GeocodingApi,
    private val apiKey: String  // Wstrzyknięty API key z resources
) {
    suspend fun getCurrentWeather(city: String): WeatherResponse {
        return weatherApi.getCurrentWeather(city, apiKey)
    }

    suspend fun getCurrentWeatherByCoordinates(latitude: Double, longitude: Double): WeatherResponse {
        return weatherApi.getCurrentWeatherByCoordinates(latitude, longitude, apiKey)
    }

    suspend fun getForecast(city: String): ForecastResponse {
        return weatherApi.getForecast(city, apiKey)
    }

    suspend fun getForecastByCoordinates(latitude: Double, longitude: Double): ForecastResponse {
        return weatherApi.getForecastByCoordinates(latitude, longitude, apiKey)
    }
    suspend fun searchCity(query: String): List<GeocodingResponse> {
        return geocodingApi.searchCity(query, 5, apiKey)
    }
    suspend fun getCurrentUVIndex(latitude: Double, longitude: Double): UVIndexResponse {
        return weatherApi.getCurrentUVIndex(latitude, longitude, apiKey)
    }
    suspend fun getForecastUVIndex(latitude: Double, longitude: Double): List<UVIndexResponse> {
        return weatherApi.getForecastUVIndex(latitude, longitude, 5, apiKey)
    }
    suspend fun getCurrentAirQuality(latitude: Double, longitude: Double): AirQualityResponse {
        return weatherApi.getCurrentAirQuality(latitude, longitude, apiKey)
    }

    suspend fun getForecastAirQuality(latitude: Double, longitude: Double): AirQualityResponse {
        return weatherApi.getForecastAirQuality(latitude, longitude, apiKey)
    }
    suspend fun getWeatherAlerts(latitude: Double, longitude: Double): AlertResponse {
        return weatherApi.getOneCallData(latitude, longitude, "minutely,hourly", apiKey)
    }
}