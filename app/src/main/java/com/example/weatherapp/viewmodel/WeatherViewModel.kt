package com.example.weatherapp.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.remote.model.ForecastResponse
import com.example.weatherapp.data.remote.model.WeatherResponse
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
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
}