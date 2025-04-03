package com.example.weatherapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapp.ui.components.CurrentWeatherCard
import com.example.weatherapp.ui.components.DailyForecastSection
import com.example.weatherapp.ui.components.HourlyForecastSection
import com.example.weatherapp.ui.components.WeatherDetailsCard
import com.example.weatherapp.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    viewModel: WeatherViewModel = hiltViewModel()

) {
    val currentWeather = viewModel.currentWeatherState.value
    val forecast = viewModel.forecastState.value
    val isLoading = viewModel.isLoading.value
    val error = viewModel.error.value

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
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(Icons.Default.Search, contentDescription = "Szukaj")
                    }
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
                    modifier = Modifier.align(Alignment.Center)
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
                    CurrentWeatherCard(currentWeather)

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
