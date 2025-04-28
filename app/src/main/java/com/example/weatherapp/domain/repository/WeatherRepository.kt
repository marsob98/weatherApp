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

    suspend fun getCurrentWeatherByCoordinates(latitude: Double, longitude: Double): WeatherResponse {
        return weatherApi.getCurrentWeatherByCoordinates(latitude, longitude, Constants.API_KEY)
    }

    suspend fun getForecast(city: String): ForecastResponse {
        return weatherApi.getForecast(city, Constants.API_KEY)
    }

    suspend fun getForecastByCoordinates(latitude: Double, longitude: Double): ForecastResponse {
        return weatherApi.getForecastByCoordinates(latitude, longitude, Constants.API_KEY)
    }

    // Metoda do wyszukiwania miast
    suspend fun searchCity(query: String): List<GeocodingResponse> {
        return geocodingApi.searchCity(query, 5, Constants.API_KEY)
    }


    // Indeks UV
    suspend fun getCurrentUVIndex(latitude: Double, longitude: Double): UVIndexResponse {
        return weatherApi.getCurrentUVIndex(latitude, longitude, Constants.API_KEY)
    }

    suspend fun getForecastUVIndex(latitude: Double, longitude: Double): List<UVIndexResponse> {
        return weatherApi.getForecastUVIndex(latitude, longitude, 5, Constants.API_KEY)
    }

    // Jakość powietrza
    suspend fun getCurrentAirQuality(latitude: Double, longitude: Double): AirQualityResponse {
        return weatherApi.getCurrentAirQuality(latitude, longitude, Constants.API_KEY)
    }

    suspend fun getForecastAirQuality(latitude: Double, longitude: Double): AirQualityResponse {
        return weatherApi.getForecastAirQuality(latitude, longitude, Constants.API_KEY)
    }

    // Alerty pogodowe i dane one-call
    suspend fun getWeatherAlerts(latitude: Double, longitude: Double): AlertResponse {
        return weatherApi.getOneCallData(latitude, longitude, "minutely,hourly", Constants.API_KEY)
    }
}