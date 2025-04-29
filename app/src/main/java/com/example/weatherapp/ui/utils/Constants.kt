package com.example.weatherapp.ui.utils

object Constants {
    // Poprawiony bazowy URL - dodajemy "data/2.5/" do ścieżki endpointów pogodowych
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    // URL dla API geocoding
    const val GEO_URL = "https://api.openweathermap.org/geo/1.0/"
    // DEFAULT_CITY zostaje, ale API_KEY będzie pobierany z zasobów
    const val DEFAULT_CITY = "Warsaw"
}