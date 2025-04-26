package com.example.weatherapp.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.remote.model.ForecastItem
import com.example.weatherapp.ui.utils.formatDateTime

@Composable
fun HourlyForecastSection(forecast: List<ForecastItem>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF3A3E59).copy(alpha = 0.7f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Prognoza godzinowa",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Text(
            text = formatDateTime(forecastItem.dt),
            style = MaterialTheme.typography.bodySmall,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Dodajemy ikonę pogody
        WeatherIcon(
            iconCode = forecastItem.weather.firstOrNull()?.icon ?: "",
            modifier = Modifier.size(28.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "${forecastItem.main.temp.toInt()}°",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }
}