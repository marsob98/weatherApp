// Plik: app/src/main/java/com/example/weatherapp/data/remote/api/WeatherApi.kt
package com.example.weatherapp.data.remote.api

import com.example.weatherapp.data.remote.model.*
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "pl"
    ): WeatherResponse

    @GET("weather")
    suspend fun getCurrentWeatherByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "pl"
    ): WeatherResponse

    @GET("forecast")
    suspend fun getForecast(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "pl"
    ): ForecastResponse

    @GET("forecast")
    suspend fun getForecastByCoordinates(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "pl"
    ): ForecastResponse

    // Indeks UV
    @GET("uvi")
    suspend fun getCurrentUVIndex(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String
    ): UVIndexResponse

    @GET("uvi/forecast")
    suspend fun getForecastUVIndex(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("cnt") count: Int = 5, // Liczba dni prognozy
        @Query("appid") apiKey: String
    ): List<UVIndexResponse>

    // Jakość powietrza
    @GET("air_pollution")
    suspend fun getCurrentAirQuality(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String
    ): AirQualityResponse

    @GET("air_pollution/forecast")
    suspend fun getForecastAirQuality(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String
    ): AirQualityResponse

    // Alerty pogodowe
    @GET("onecall")
    suspend fun getOneCallData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("exclude") exclude: String = "minutely,hourly", // Wykluczamy niepotrzebne dane
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "pl"
    ): AlertResponse
}