// app/src/main/java/com/example/weatherapp/viewmodel/WeatherViewModel.kt

package com.example.weatherapp.viewmodel

import android.content.Context
import android.location.Location
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.R
import com.example.weatherapp.data.remote.model.*
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.ui.utils.Constants
import com.example.weatherapp.utils.LocationManager
import com.example.weatherapp.utils.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationManager: LocationManager,
    @ApplicationContext private val context: Context
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

    // Stan połączenia z internetem
    private val _isNetworkAvailable = mutableStateOf(false)
    val isNetworkAvailable: State<Boolean> = _isNetworkAvailable

    // Nowe stany dla dodatkowych funkcjonalności
    private val _uvIndexState = mutableStateOf<UVIndexResponse?>(null)
    val uvIndexState: State<UVIndexResponse?> = _uvIndexState

    private val _uvForecastState = mutableStateOf<List<UVIndexResponse>?>(null)
    val uvForecastState: State<List<UVIndexResponse>?> = _uvForecastState

    private val _airQualityState = mutableStateOf<AirQualityResponse?>(null)
    val airQualityState: State<AirQualityResponse?> = _airQualityState

    private val _alertsState = mutableStateOf<List<Alert>?>(null)
    val alertsState: State<List<Alert>?> = _alertsState

    private val _astronomicalData = mutableStateOf<AstronomicalData?>(null)
    val astronomicalData: State<AstronomicalData?> = _astronomicalData

    private val _precipitationData = mutableStateOf<PrecipitationInfo?>(null)
    val precipitationData: State<PrecipitationInfo?> = _precipitationData

    init {
        checkNetworkAvailability()
        checkLocationPermission()
        if (locationManager.hasLocationPermission()) {
            getWeatherForCurrentLocation(true)
        } else {
            getWeatherForCity(Constants.DEFAULT_CITY)
        }

        // Nasłuchuj aktualizacji lokalizacji
        viewModelScope.launch {
            locationManager.locationUpdates()
                .catch { e ->
                    _error.value = context.getString(R.string.error_location, e.localizedMessage ?: "Unknown error")
                }
                .collect { location ->
                    _currentLocation.value = location
                    updateLocationWeather(location)
                }
        }
    }

    fun checkNetworkAvailability() {
        _isNetworkAvailable.value = NetworkUtils.isNetworkAvailable(context)
    }

    fun checkLocationPermission() {
        _locationPermissionGranted.value = locationManager.hasLocationPermission()
    }

    // Dodajemy nowy parametr, który określa czy ustawić tę lokalizację jako główną
    fun getWeatherForCurrentLocation(setAsMain: Boolean = false) {
        viewModelScope.launch {
            try {
                _isLocationLoading.value = true
                _error.value = null

                // Sprawdź połączenie z internetem
                if (!NetworkUtils.isNetworkAvailable(context)) {
                    _error.value = context.getString(R.string.error_network)
                    _isLocationLoading.value = false
                    return@launch
                }

                val location = locationManager.getLastLocation()
                if (location != null) {
                    _currentLocation.value = location

                    // Jeśli setAsMain jest true, pobieramy dane dla głównego widoku
                    if (setAsMain) {
                        getWeatherForLocation(location, true)
                    } else {
                        // Zawsze aktualizujemy dane dla karty lokalizacji
                        updateLocationWeather(location)
                    }
                } else {
                    _error.value = context.getString(R.string.error_no_location_data)
                    // Jeśli nie możemy uzyskać lokalizacji, użyj domyślnego miasta
                    if (_currentWeatherState.value == null) {
                        getWeatherForCity(Constants.DEFAULT_CITY)
                    }
                }
            } catch (e: Exception) {
                handleException(e)
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

            // Sprawdź połączenie z internetem
            if (!NetworkUtils.isNetworkAvailable(context)) {
                return
            }

            val weatherResponse = repository.getCurrentWeatherByCoordinates(
                location.latitude,
                location.longitude
            )
            _locationWeatherState.value = weatherResponse
        } catch (e: Exception) {
            // Błędy w aktualizacji karty lokalizacji logujemy, ale nie pokazujemy użytkownikowi
            // gdyż nie powinny wpływać na główny widok
            e.printStackTrace()
        } finally {
            _isLocationLoading.value = false
        }
    }

    // Dodajemy parametr setAsMain, który określa czy ustawić tę lokalizację jako główną
    private suspend fun getWeatherForLocation(location: Location, setAsMain: Boolean = false) {
        try {
            _isLoading.value = true

            // Sprawdź połączenie z internetem
            if (!NetworkUtils.isNetworkAvailable(context)) {
                _error.value = context.getString(R.string.error_network)
                return
            }

            val weatherResponse = repository.getCurrentWeatherByCoordinates(
                location.latitude,
                location.longitude
            )

            // Jeśli setAsMain jest true, ustawiamy dane jako główne
            if (setAsMain) {
                _currentWeatherState.value = weatherResponse
                _astronomicalData.value = AstronomicalData.fromWeatherResponse(weatherResponse)
            }

            _locationWeatherState.value = weatherResponse

            val forecastResponse = repository.getForecastByCoordinates(
                location.latitude,
                location.longitude
            )

            // Jeśli setAsMain jest true, ustawiamy również prognozę jako główną
            if (setAsMain) {
                _forecastState.value = forecastResponse

                // Pobieramy dodatkowe dane
                getUVIndex(location.latitude, location.longitude)
                getAirQuality(location.latitude, location.longitude)
                getWeatherAlerts(location.latitude, location.longitude)
            }

        } catch (e: Exception) {
            handleException(e)
        } finally {
            _isLoading.value = false
        }
    }

    fun getWeatherForCity(city: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                // Sprawdź połączenie z internetem
                if (!NetworkUtils.isNetworkAvailable(context)) {
                    _error.value = context.getString(R.string.error_network)
                    _isLoading.value = false
                    return@launch
                }

                val weatherResponse = repository.getCurrentWeather(city)
                _currentWeatherState.value = weatherResponse
                _astronomicalData.value = AstronomicalData.fromWeatherResponse(weatherResponse)

                val forecastResponse = repository.getForecast(city)
                _forecastState.value = forecastResponse

                // Pobieramy dodatkowe dane
                weatherResponse.coord.let { coord ->
                    getUVIndex(coord.lat, coord.lon)
                    getAirQuality(coord.lat, coord.lon)
                    getWeatherAlerts(coord.lat, coord.lon)
                }

            } catch (e: Exception) {
                handleException(e)
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
                _error.value = null

                // Sprawdź połączenie z internetem
                if (!NetworkUtils.isNetworkAvailable(context)) {
                    _error.value = context.getString(R.string.error_network)
                    _isSearching.value = false
                    return@launch
                }

                val results = repository.searchCity(query)
                _searchResults.value = results
            } catch (e: Exception) {
                _error.value = context.getString(R.string.error_search, e.localizedMessage ?: "Unknown error")
                _searchResults.value = emptyList()
            } finally {
                _isSearching.value = false
            }
        }
    }

    // Metody do pobierania nowych danych z lepszą obsługą błędów
    fun getUVIndex(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                // Sprawdź połączenie z internetem
                if (!NetworkUtils.isNetworkAvailable(context)) {
                    return@launch
                }

                val response = repository.getCurrentUVIndex(latitude, longitude)
                _uvIndexState.value = response

                val forecast = repository.getForecastUVIndex(latitude, longitude)
                _uvForecastState.value = forecast
            } catch (e: Exception) {
                // Błąd pobierania UV nie powinien blokować głównego UI, ale logujemy go
                e.printStackTrace()
            }
        }
    }

    fun getAirQuality(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                // Sprawdź połączenie z internetem
                if (!NetworkUtils.isNetworkAvailable(context)) {
                    return@launch
                }

                val response = repository.getCurrentAirQuality(latitude, longitude)
                _airQualityState.value = response
            } catch (e: Exception) {
                // Błąd pobierania jakości powietrza nie powinien blokować głównego UI, ale logujemy go
                e.printStackTrace()
            }
        }
    }

    fun getWeatherAlerts(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                // Sprawdź połączenie z internetem
                if (!NetworkUtils.isNetworkAvailable(context)) {
                    return@launch
                }

                val response = repository.getWeatherAlerts(latitude, longitude)
                _alertsState.value = response.alerts
            } catch (e: Exception) {
                // Błąd pobierania alertów nie powinien blokować głównego UI, ale logujemy go
                e.printStackTrace()
            }
        }
    }

    /**
     * Pobiera dane dla konkretnego dnia na podstawie timestampu
     */
    fun getDayData(timestamp: Long) {
        viewModelScope.launch {
            try {
                // Sprawdź połączenie z internetem
                if (!NetworkUtils.isNetworkAvailable(context)) {
                    _error.value = context.getString(R.string.error_network)
                    return@launch
                }

                val calendar = Calendar.getInstance()
                calendar.timeInMillis = timestamp * 1000

                // Pobierz dane z forecast state
                val dayForecast = _forecastState.value?.list?.filter {
                    val forecastCal = Calendar.getInstance()
                    forecastCal.timeInMillis = it.dt * 1000

                    forecastCal.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR) &&
                            forecastCal.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                } ?: emptyList()

                if (dayForecast.isNotEmpty()) {
                    // Użyj pierwszego elementu prognozy, aby uzyskać współrzędne
                    val cityCoord = _forecastState.value?.city?.coord

                    if (cityCoord != null) {
                        // Pobierz dodatkowe dane dla wybranego dnia, używając współrzędnych miasta
                        getUVIndex(cityCoord.lat, cityCoord.lon)
                        getAirQuality(cityCoord.lat, cityCoord.lon)
                        getWeatherAlerts(cityCoord.lat, cityCoord.lon)
                    }
                }
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    // Funkcja pomocnicza do obsługi różnych typów wyjątków
    private fun handleException(e: Exception) {
        when (e) {
            is IOException -> _error.value = context.getString(R.string.error_network)
            is HttpException -> {
                when (e.code()) {
                    404 -> _error.value = context.getString(R.string.error_loading_weather)
                    else -> _error.value = "${e.code()}: ${e.message()}"
                }
            }
            else -> _error.value = context.getString(R.string.error_generic, e.localizedMessage ?: "Unknown error")
        }
        e.printStackTrace()
    }
}