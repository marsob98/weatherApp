// Plik: app/src/main/java/com/example/weatherapp/viewmodel/WeatherViewModel.kt

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

    private val _locationWeatherState = mutableStateOf<WeatherResponse?>(null)
    val locationWeatherState: State<WeatherResponse?> = _locationWeatherState

    private val _isLocationLoading = mutableStateOf(false)
    val isLocationLoading: State<Boolean> = _isLocationLoading

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
                    updateLocationWeather(location)
                }
        }
    }

    fun checkLocationPermission() {
        _locationPermissionGranted.value = locationManager.hasLocationPermission()
    }

    fun getWeatherForCurrentLocation() {
        viewModelScope.launch {
            try {
                _isLocationLoading.value = true
                _error.value = null

                val location = locationManager.getLastLocation()
                if (location != null) {
                    _currentLocation.value = location

                    // Pobieramy dane tylko dla głównego widoku, jeśli jeszcze nie mamy danych
                    if (_currentWeatherState.value == null) {
                        getWeatherForLocation(location)
                    }

                    // Zawsze aktualizujemy dane dla karty lokalizacji
                    updateLocationWeather(location)
                } else {
                    // Jeśli nie możemy uzyskać lokalizacji, użyj domyślnego miasta
                    if (_currentWeatherState.value == null) {
                        getWeatherForCity(Constants.DEFAULT_CITY)
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message
                if (_currentWeatherState.value == null) {
                    getWeatherForCity(Constants.DEFAULT_CITY)
                }
            } finally {
                _isLocationLoading.value = false
            }
        }
    }

    private suspend fun updateLocationWeather(location: Location) {
        try {
            _isLocationLoading.value = true
            val weatherResponse = repository.getCurrentWeatherByCoordinates(
                location.latitude,
                location.longitude
            )
            _locationWeatherState.value = weatherResponse
        } catch (e: Exception) {
            // Błędy w aktualizacji karty lokalizacji nie powinny wpływać na główny widok
            // więc nie ustawiamy _error.value
        } finally {
            _isLocationLoading.value = false
        }
    }

    private suspend fun getWeatherForLocation(location: Location) {
        try {
            _isLoading.value = true
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
        } finally {
            _isLoading.value = false
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