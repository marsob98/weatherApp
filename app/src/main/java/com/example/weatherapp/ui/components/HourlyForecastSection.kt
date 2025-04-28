// app/src/main/java/com/example/weatherapp/ui/components/HourlyForecastSection.kt
package com.example.weatherapp.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.remote.model.ForecastItem
import com.example.weatherapp.ui.theme.LocalWeatherColors
import com.example.weatherapp.ui.utils.formatDateTime

@Composable
fun HourlyForecastSection(forecast: List<ForecastItem>) {
    val weatherColors = LocalWeatherColors.current

    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        backgroundColor = weatherColors.cardBackground,
        borderColor = weatherColors.textPrimary.copy(alpha = 0.1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Prognoza godzinowa",
                style = MaterialTheme.typography.titleLarge,
                color = weatherColors.textPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .fillMaxWidth()
            ) {
                forecast.take(24).forEach { item ->
                    HourlyForecastItem(item)
                }
            }
        }
    }
}

@Composable
fun HourlyForecastItem(forecastItem: ForecastItem) {
    val weatherColors = LocalWeatherColors.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 12.dp)
    ) {
        Text(
            text = formatDateTime(forecastItem.dt),
            style = MaterialTheme.typography.bodySmall,
            color = weatherColors.textSecondary
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Dodajemy animowaną ikonę pogody
        WeatherIcon(
            iconCode = forecastItem.weather.firstOrNull()?.icon ?: "",
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "${forecastItem.main.temp.toInt()}°",
            style = MaterialTheme.typography.titleMedium,
            color = weatherColors.textPrimary,
            fontWeight = FontWeight.SemiBold
        )
    }
}