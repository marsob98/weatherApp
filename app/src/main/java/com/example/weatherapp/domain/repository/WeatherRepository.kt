// Plik: app/src/main/java/com/example/weatherapp/domain/repository/WeatherRepository.kt
package com.example.weatherapp.domain.repository

import com.example.weatherapp.data.remote.api.GeocodingApi
import com.example.weatherapp.data.remote.api.WeatherApi
import com.example.weatherapp.data.remote.model.ForecastResponse
import com.example.weatherapp.data.remote.model.GeocodingResponse
import com.example.weatherapp.data.remote.model.WeatherResponse
import com.example.weatherapp.ui.utils.Constants
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val geocodingApi: GeocodingApi
) {
    suspend fun getCurrentWeather(city: String): WeatherResponse {
        return weatherApi.getCurrentWeather(city, Constants.API_KEY)
    }

    suspend fun getForecast(city: String): ForecastResponse {
        return weatherApi.getForecast(city, Constants.API_KEY)
    }

    // Metoda do wyszukiwania miast
    suspend fun searchCity(query: String): List<GeocodingResponse> {
        return geocodingApi.searchCity(query, 5, Constants.API_KEY)
    }
}