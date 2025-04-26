// Plik: app/src/main/java/com/example/weatherapp/ui/utils/Constants.kt
package com.example.weatherapp.ui.utils

object Constants {
    // Poprawiony bazowy URL - dodajemy "data/2.5/" do ścieżki endpointów pogodowych
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    // URL dla API geocoding
    const val GEO_URL = "https://api.openweathermap.org/geo/1.0/"
    const val API_KEY = "50327eacbb2804a827daa19fdfa0a11e"
    const val DEFAULT_CITY = "Warsaw"
}