package com.example.weatherapp.viewmodel

import android.location.Location
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.remote.model.ForecastResponse
import com.example.weatherapp.data.remote.model.GeocodingResponse
import com.example.weatherapp.data.remote.model.WeatherResponse
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.ui.utils.Constants
import com.example.weatherapp.utils.LocationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationManager: LocationManager
) : ViewModel() {

    private val _currentWeatherState = mutableStateOf<WeatherResponse?>(null)
    val currentWeatherState: State<WeatherResponse?> = _currentWeatherState

    private val _forecastState = mutableStateOf<ForecastResponse?>(null)
    val forecastState: State<ForecastResponse?> = _forecastState

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    private val _searchResults = MutableStateFlow<List<GeocodingResponse>>(emptyList())
    val searchResults: StateFlow<List<GeocodingResponse>> = _searchResults

    private val _isSearching = mutableStateOf(false)
    val isSearching: State<Boolean> = _isSearching

    private val _locationPermissionGranted = mutableStateOf(false)
    val locationPermissionGranted: State<Boolean> = _locationPermissionGranted

    private val _currentLocation = mutableStateOf<Location?>(null)
    val currentLocation: State<Location?> = _currentLocation

    init {
        checkLocationPermission()
        if (locationManager.hasLocationPermission()) {
            getWeatherForCurrentLocation()
        } else {
            getWeatherForCity(Constants.DEFAULT_CITY)
        }

        // Nasłuchuj aktualizacji lokalizacji
        viewModelScope.launch {
            locationManager.locationUpdates()
                .catch { e ->
                    _error.value = "Błąd lokalizacji: ${e.message}"
                }
                .collect { location ->
                    _currentLocation.value = location
                    getWeatherForLocation(location)
                }
        }
    }

    fun checkLocationPermission() {
        _locationPermissionGranted.value = locationManager.hasLocationPermission()
    }

    fun getWeatherForCurrentLocation() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val location = locationManager.getLastLocation()
                if (location != null) {
                    _currentLocation.value = location
                    getWeatherForLocation(location)
                } else {
                    // Jeśli nie możemy uzyskać lokalizacji, użyj domyślnego miasta
                    getWeatherForCity(Constants.DEFAULT_CITY)
                }
            } catch (e: Exception) {
                _error.value = e.message
                getWeatherForCity(Constants.DEFAULT_CITY)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun getWeatherForLocation(location: Location) {
        try {
            val weatherResponse = repository.getCurrentWeatherByCoordinates(
                location.latitude,
                location.longitude
            )
            _currentWeatherState.value = weatherResponse

            val forecastResponse = repository.getForecastByCoordinates(
                location.latitude,
                location.longitude
            )
            _forecastState.value = forecastResponse
        } catch (e: Exception) {
            _error.value = e.message
        }
    }

    fun getWeatherForCity(city: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val weatherResponse = repository.getCurrentWeather(city)
                _currentWeatherState.value = weatherResponse

                val forecastResponse = repository.getForecast(city)
                _forecastState.value = forecastResponse

            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Funkcja wyszukiwania miast
    fun searchCities(query: String) {
        if (query.length < 3) {
            _searchResults.value = emptyList()
            return
        }

        viewModelScope.launch {
            try {
                _isSearching.value = true
                println("Wyszukiwanie miast dla zapytania: $query")
                val results = repository.searchCity(query)
                println("Otrzymano wyniki: ${results.size}")
                _searchResults.value = results
            } catch (e: Exception) {
                println("Błąd podczas wyszukiwania: ${e.message}")
                _error.value = e.message
                _searchResults.value = emptyList()
            } finally {
                _isSearching.value = false
            }
        }
    }
}