package com.example.weatherapp.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.remote.model.ForecastResponse
import com.example.weatherapp.data.remote.model.GeocodingResponse
import com.example.weatherapp.data.remote.model.WeatherResponse
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.ui.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _currentWeatherState = mutableStateOf<WeatherResponse?>(null)
    val currentWeatherState: State<WeatherResponse?> = _currentWeatherState

    private val _forecastState = mutableStateOf<ForecastResponse?>(null)
    val forecastState: State<ForecastResponse?> = _forecastState

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    // Dodajemy nowy stan dla wyszukiwania miast
    private val _searchResults = MutableStateFlow<List<GeocodingResponse>>(emptyList())
    val searchResults: StateFlow<List<GeocodingResponse>> = _searchResults

    private val _isSearching = mutableStateOf(false)
    val isSearching: State<Boolean> = _isSearching

    init {
        getWeatherForCity(Constants.DEFAULT_CITY)
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

    // Nowa funkcja do wyszukiwania miast
    fun searchCities(query: String) {
        if (query.length < 3) {
            _searchResults.value = emptyList()
            return
        }

        viewModelScope.launch {
            try {
                _isSearching.value = true
                val results = repository.searchCity(query)
                _searchResults.value = results
            } catch (e: Exception) {
                // Obsługa błędu (opcjonalnie wyświetlenie komunikatu)
            } finally {
                _isSearching.value = false
            }
        }
    }
}