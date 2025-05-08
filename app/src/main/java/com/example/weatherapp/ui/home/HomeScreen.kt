package com.example.weatherapp.ui.home

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.weatherapp.ui.WeatherNavigation
import com.example.weatherapp.ui.Screen
import com.example.weatherapp.ui.components.*
import com.example.weatherapp.viewmodel.FavouriteViewModel
import com.example.weatherapp.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    weatherViewModel: WeatherViewModel,
    navController: NavController,
    onNavigateToSearch: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToDayDetails: (Long) -> Unit,
    favouriteViewModel: FavouriteViewModel = hiltViewModel()
) {
    val currentWeather = weatherViewModel.currentWeatherState.value
    val forecast = weatherViewModel.forecastState.value
    val isLoading = weatherViewModel.isLoading.value
    val error = weatherViewModel.error.value
    val locationPermissionGranted = weatherViewModel.locationPermissionGranted.value
    val locationWeather = weatherViewModel.locationWeatherState.value
    val isLocationLoading = weatherViewModel.isLocationLoading.value
    val favourites by favouriteViewModel.favourites.collectAsState(initial = emptyList())
    val uvIndex = weatherViewModel.uvIndexState.value
    val airQuality = weatherViewModel.airQualityState.value
    val alerts = weatherViewModel.alertsState.value
    val astronomicalData = weatherViewModel.astronomicalData.value
    val precipitationData = weatherViewModel.precipitationData.value

    val backgroundBrush = remember {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF2E3346),
                Color(0xFF1C1B33)
            )
        )
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(currentWeather?.name ?: "Pogoda") },
                actions = {
                    // Przycisk do wyszukiwania
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(Icons.Default.Search, contentDescription = "Szukaj")
                    }

                    // Przycisk do przejścia do ekranu ulubionych
                    IconButton(onClick = onNavigateToFavorites) {
                        Icon(Icons.Default.Favorite, contentDescription = "Ulubione")
                    }
                }
            )
        }
    ) { paddingValues ->
        LocationPermission(
            permissions = listOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            onPermissionGranted = {
                weatherViewModel.checkLocationPermission()
                weatherViewModel.getWeatherForCurrentLocation()
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = backgroundBrush)
                    .padding(paddingValues)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White
                    )
                } else if (error != null) {
                    Text(
                        text = "Wystąpił błąd: $error",
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                } else if (currentWeather != null && forecast != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(bottom = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Karta pogody opartej na lokalizacji
                        if (locationPermissionGranted) {
                            LocationWeatherCard(
                                weather = locationWeather,
                                isLoading = isLocationLoading,
                                onRefreshLocation = { weatherViewModel.getWeatherForCurrentLocation(true) }
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Karta obecnej pogody
                        CurrentWeatherCard(
                            weather = currentWeather,
                            favouriteViewModel = favouriteViewModel
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Prognoza godzinowa
                        HourlyForecastSection(forecast.list.take(24))

                        Spacer(modifier = Modifier.height(16.dp))

                        // Prognoza dzienna
                        DailyForecastSection(
                            forecast = forecast,
                            onDayClick = onNavigateToDayDetails
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Szczegóły pogody
                        WeatherDetailsCard(currentWeather)

                        Spacer(modifier = Modifier.height(16.dp))

                        // Nowe komponenty

                        // Indeks UV
                        if (uvIndex != null) {
                            UVIndexCard(uvIndex = uvIndex)
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Jakość powietrza
                        if (airQuality != null) {
                            AirQualityCard(airQuality = airQuality)
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Dane astronomiczne
                        if (astronomicalData != null) {
                            AstronomicalCard(astronomicalData = astronomicalData)
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Informacje o opadach
                        if (precipitationData != null) {
                            PrecipitationCard(precipitationInfo = precipitationData)
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Alerty pogodowe
                        if (alerts != null && alerts.isNotEmpty()) {
                            WeatherAlertsCard(
                                alerts = alerts,
                                onViewAllAlerts = {
                                    navController.navigate(Screen.Alerts.route)
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Prognoza długoterminowa
                        if (forecast != null) {
                            LongTermForecastSection(
                                forecast = forecast,
                                onDayClick = onNavigateToDayDetails
                            )
                        }
                    }
                }
            }
        }
    }
}