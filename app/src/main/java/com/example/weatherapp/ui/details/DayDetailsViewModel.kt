package com.example.weatherapp.ui.details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.remote.model.*
import com.example.weatherapp.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

/**
 * ViewModel dedykowany do ekranu szczegółów dnia
 * Odpowiada za pobranie wszystkich danych pogodowych dla wybranego dnia
 */
@HiltViewModel
class DayDetailsViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    private val _dayForecast = mutableStateOf<List<ForecastItem>>(emptyList())
    val dayForecast: State<List<ForecastItem>> = _dayForecast

    private val _uvIndex = mutableStateOf<UVIndexResponse?>(null)
    val uvIndex: State<UVIndexResponse?> = _uvIndex

    private val _airQuality = mutableStateOf<AirQualityResponse?>(null)
    val airQuality: State<AirQualityResponse?> = _airQuality

    private val _alerts = mutableStateOf<List<Alert>?>(null)
    val alerts: State<List<Alert>?> = _alerts

    private val _astronomicalData = mutableStateOf<AstronomicalData?>(null)
    val astronomicalData: State<AstronomicalData?> = _astronomicalData

    private val _precipitationData = mutableStateOf<PrecipitationInfo?>(null)
    val precipitationData: State<PrecipitationInfo?> = _precipitationData

    /**
     * Filtruje główną prognozę, aby pokazać tylko dane dla wybranego dnia
     */
    fun filterForecastForDay(allForecast: List<ForecastItem>, date: Long) {
        val calendar = Calendar.getInstance().apply { timeInMillis = date * 1000 }

        val dayItems = allForecast.filter {
            val forecastCal = Calendar.getInstance().apply { timeInMillis = it.dt * 1000 }

            forecastCal.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR) &&
                    forecastCal.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
        }

        _dayForecast.value = dayItems
    }

    /**
     * Pobiera wszystkie potrzebne dane dla wybranego dnia
     */
    fun loadDayDetails(cityName: String?, latitude: Double?, longitude: Double?) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                // Jeśli mamy współrzędne geograficzne, używamy ich; w przeciwnym razie używamy nazwy miasta
                if (latitude != null && longitude != null) {
                    loadDataByCoordinates(latitude, longitude)
                } else if (!cityName.isNullOrEmpty()) {
                    loadDataByCity(cityName)
                } else {
                    _error.value = "Brak danych lokalizacji"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun loadDataByCoordinates(latitude: Double, longitude: Double) {
        // Pobranie indeksu UV
        try {
            val uvResponse = repository.getCurrentUVIndex(latitude, longitude)
            _uvIndex.value = uvResponse
        } catch (e: Exception) {
            // Nie ustawiaj błędu globalnego - to nie jest krytyczne
        }

        // Pobranie jakości powietrza
        try {
            val airQualityResponse = repository.getCurrentAirQuality(latitude, longitude)
            _airQuality.value = airQualityResponse
        } catch (e: Exception) {
            // Nie ustawiaj błędu globalnego - to nie jest krytyczne
        }

        // Pobranie alertów pogodowych
        try {
            val alertsResponse = repository.getWeatherAlerts(latitude, longitude)
            _alerts.value = alertsResponse.alerts
        } catch (e: Exception) {
            // Nie ustawiaj błędu globalnego - to nie jest krytyczne
        }
    }

    private suspend fun loadDataByCity(cityName: String) {
        try {
            // Pobierz najpierw podstawowe dane pogodowe, aby uzyskać współrzędne
            val weatherResponse = repository.getCurrentWeather(cityName)

            // Z odpowiedzi pobierz współrzędne
            val coord = weatherResponse.coord

            // Utwórz dane astronomiczne na podstawie odpowiedzi
            _astronomicalData.value = AstronomicalData.fromWeatherResponse(weatherResponse)

            // Pobierz pozostałe dane używając współrzędnych
            loadDataByCoordinates(coord.lat, coord.lon)

        } catch (e: Exception) {
            _error.value = e.message
        }
    }
}