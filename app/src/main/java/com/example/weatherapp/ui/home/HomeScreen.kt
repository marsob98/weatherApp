// Plik: app/src/main/java/com/example/weatherapp/ui/home/HomeScreen.kt
package com.example.weatherapp.ui.home

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
import com.example.weatherapp.ui.components.CurrentWeatherCard
import com.example.weatherapp.ui.components.DailyForecastSection
import com.example.weatherapp.ui.components.HourlyForecastSection
import com.example.weatherapp.ui.components.WeatherDetailsCard
import com.example.weatherapp.viewmodel.FavouriteViewModel
import com.example.weatherapp.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    weatherViewModel: WeatherViewModel,
    onNavigateToSearch: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    favouriteViewModel: FavouriteViewModel = hiltViewModel()
) {
    val currentWeather = weatherViewModel.currentWeatherState.value
    val forecast = weatherViewModel.forecastState.value
    val isLoading = weatherViewModel.isLoading.value
    val error = weatherViewModel.error.value

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
                    // Usunięta ikona gwiazdki, zostawiamy tylko dwie akcje

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
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CurrentWeatherCard(
                        weather = currentWeather,
                        favouriteViewModel = favouriteViewModel
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    HourlyForecastSection(forecast.list.take(24))

                    Spacer(modifier = Modifier.height(16.dp))

                    DailyForecastSection(forecast)

                    Spacer(modifier = Modifier.height(16.dp))

                    WeatherDetailsCard(currentWeather)
                }
            }
        }
    }
}