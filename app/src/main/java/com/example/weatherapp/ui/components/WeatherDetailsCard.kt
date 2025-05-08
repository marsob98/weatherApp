package com.example.weatherapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.remote.model.WeatherResponse
import com.example.weatherapp.ui.theme.LocalWeatherColors

@Composable
fun WeatherDetailsCard(weather: WeatherResponse) {
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
                text = "Szczegóły",
                style = MaterialTheme.typography.titleLarge,
                color = weatherColors.textPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DetailItem(
                    label = "Wilgotność",
                    value = "${weather.main.humidity}%"
                )

                DetailItem(
                    label = "Ciśnienie",
                    value = "${weather.main.pressure} hPa"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DetailItem(
                    label = "Wiatr",
                    value = "${weather.wind.speed} m/s"
                )

                DetailItem(
                    label = "Widoczność",
                    value = "${weather.visibility / 1000} km"
                )
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    val weatherColors = LocalWeatherColors.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = weatherColors.textSecondary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = weatherColors.textPrimary,
            fontWeight = FontWeight.Medium
        )
    }
}