// Plik: app/src/main/java/com/example/weatherapp/ui/details/DayDetailsScreen.kt
package com.example.weatherapp.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.remote.model.*
import com.example.weatherapp.ui.components.*
import com.example.weatherapp.ui.theme.LocalWeatherColors
import com.example.weatherapp.ui.utils.formatDate
import com.example.weatherapp.ui.utils.formatDateTime
import com.example.weatherapp.viewmodel.WeatherViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayDetailsScreen(
    date: Long,
    forecastItems: List<ForecastItem>,
    onNavigateBack: () -> Unit,
    weatherViewModel: WeatherViewModel
) {
    val weatherColors = LocalWeatherColors.current
    val currentWeather = weatherViewModel.currentWeatherState.value
    val cityName = currentWeather?.name ?: ""

    // Filtrujemy prognozę dla wybranego dnia
    val dayForecast = remember(forecastItems, date) {
        forecastItems.filter {
            val calendar1 = Calendar.getInstance().apply { timeInMillis = it.dt * 1000 }
            val calendar2 = Calendar.getInstance().apply { timeInMillis = date * 1000 }

            calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR) &&
                    calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
        }
    }

    // Pobieramy dane z WeatherViewModel
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
                title = { Text(formatDate(date)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Powrót")
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
            if (dayForecast.isEmpty()) {
                Text(
                    text = "Brak danych dla wybranego dnia",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Dodajemy nazwę miasta na górze
                    if (cityName.isNotEmpty()) {
                        Text(
                            text = cityName,
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        )
                    }

                    // Podsumowanie dnia
                    DaySummaryCard(dayForecast)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Dane astronomiczne
                    if (astronomicalData != null) {
                        AstronomicalCard(astronomicalData = astronomicalData)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

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

                    // Informacje o opadach
                    if (precipitationData != null) {
                        PrecipitationCard(precipitationInfo = precipitationData)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Alerty pogodowe
                    if (alerts != null && alerts.isNotEmpty()) {
                        WeatherAlertsCard(
                            alerts = alerts,
                            onViewAllAlerts = {}  // W widoku szczegółowym nie potrzebujemy nawigacji
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Godzinowe prognozy
                    HourlyDetailCard(dayForecast)
                }
            }
        }
    }
}

@Composable
fun DaySummaryCard(forecasts: List<ForecastItem>) {
    val weatherColors = LocalWeatherColors.current

    // Obliczanie wartości min/max dla dnia
    val maxTemp = forecasts.maxOfOrNull { it.main.temp }?.toInt() ?: 0
    val minTemp = forecasts.minOfOrNull { it.main.temp }?.toInt() ?: 0
    val mostFrequentWeather = forecasts
        .groupBy { it.weather.firstOrNull()?.main }
        .maxByOrNull { it.value.size }
        ?.value?.firstOrNull()?.weather?.firstOrNull()

    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = weatherColors.cardBackground,
        borderColor = weatherColors.textPrimary.copy(alpha = 0.1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Podsumowanie dnia",
                style = MaterialTheme.typography.titleLarge,
                color = weatherColors.textPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                WeatherIcon(
                    iconCode = mostFrequentWeather?.icon ?: "",
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = mostFrequentWeather?.description?.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                        } ?: "",
                        style = MaterialTheme.typography.titleMedium,
                        color = weatherColors.textPrimary
                    )

                    Text(
                        text = "Min: ${minTemp}° • Max: ${maxTemp}°",
                        style = MaterialTheme.typography.bodyLarge,
                        color = weatherColors.textPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WeatherStatItem(
                    title = "Wilgotność",
                    value = "${forecasts.map { it.main.humidity }.average().toInt()}%"
                )

                WeatherStatItem(
                    title = "Wiatr",
                    value = "${forecasts.map { it.wind.speed }.average().toInt()} m/s"
                )

                WeatherStatItem(
                    title = "Zachmurzenie",
                    value = "${forecasts.map { it.clouds.all }.average().toInt()}%"
                )
            }
        }
    }
}

@Composable
fun WeatherStatItem(title: String, value: String) {
    val weatherColors = LocalWeatherColors.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = weatherColors.textSecondary
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = weatherColors.textPrimary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun HourlyDetailCard(forecasts: List<ForecastItem>) {
    val weatherColors = LocalWeatherColors.current

    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = weatherColors.cardBackground,
        borderColor = weatherColors.textPrimary.copy(alpha = 0.1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Prognoza godzinowa",
                style = MaterialTheme.typography.titleLarge,
                color = weatherColors.textPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            forecasts.forEach { forecast ->
                HourlyDetailRow(forecast)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun HourlyDetailRow(forecast: ForecastItem) {
    val weatherColors = LocalWeatherColors.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = formatDateTime(forecast.dt),
            style = MaterialTheme.typography.bodyLarge,
            color = weatherColors.textPrimary,
            modifier = Modifier.width(60.dp)
        )

        WeatherIcon(
            iconCode = forecast.weather.firstOrNull()?.icon ?: "",
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = forecast.weather.firstOrNull()?.description?.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                } ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = weatherColors.textPrimary
            )

            Row {
                Text(
                    text = "Wilg: ${forecast.main.humidity}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = weatherColors.textSecondary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Wiatr: ${forecast.wind.speed} m/s",
                    style = MaterialTheme.typography.bodySmall,
                    color = weatherColors.textSecondary
                )
            }
        }

        Text(
            text = "${forecast.main.temp.toInt()}°",
            style = MaterialTheme.typography.titleLarge,
            color = weatherColors.textPrimary,
            fontWeight = FontWeight.Bold
        )
    }
}