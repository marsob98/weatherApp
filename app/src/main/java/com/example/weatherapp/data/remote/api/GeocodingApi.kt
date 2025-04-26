// Nowy plik: app/src/main/java/com/example/weatherapp/data/remote/api/GeocodingApi.kt
package com.example.weatherapp.data.remote.api

import com.example.weatherapp.data.remote.model.GeocodingResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApi {
    @GET("direct")
    suspend fun searchCity(
        @Query("q") query: String,
        @Query("limit") limit: Int = 5,
        @Query("appid") apiKey: String
    ): List<GeocodingResponse>
}