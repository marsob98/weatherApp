package com.example.weatherapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.remote.model.AirQualityComponents
import com.example.weatherapp.data.remote.model.AirQualityInfo
import com.example.weatherapp.data.remote.model.AirQualityResponse
import com.example.weatherapp.ui.theme.LocalWeatherColors

@Composable
fun AirQualityCard(airQuality: AirQualityResponse?) {
    if (airQuality == null || airQuality.list.isEmpty()) return

    val weatherColors = LocalWeatherColors.current
    val airQualityData = airQuality.list.first()
    val aqiInfo = AirQualityInfo.getAirQualityInfo(airQualityData.main.aqi)

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
                .padding(16.dp)
        ) {
            Text(
                text = "Jakość powietrza",
                style = MaterialTheme.typography.titleLarge,
                color = weatherColors.textPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Wskaźnik jakości powietrza
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            color = Color(aqiInfo.color),
                            shape = RoundedCornerShape(40.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = airQualityData.main.aqi.toString(),
                        style = MaterialTheme.typography.displayMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = aqiInfo.level,
                        style = MaterialTheme.typography.titleMedium,
                        color = weatherColors.textPrimary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = aqiInfo.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = weatherColors.textSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Zalecenia:",
                style = MaterialTheme.typography.bodyLarge,
                color = weatherColors.textPrimary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = aqiInfo.recommendation,
                style = MaterialTheme.typography.bodyMedium,
                color = weatherColors.textSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Szczegółowe pomiary:",
                style = MaterialTheme.typography.bodyLarge,
                color = weatherColors.textPrimary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Wyświetlamy szczegółowe pomiary
            AirQualityDetailsGrid(airQualityData.components)
        }
    }
}

@Composable
fun AirQualityDetailsGrid(components: AirQualityComponents) {
    val weatherColors = LocalWeatherColors.current

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            AirQualityDetailItem(
                title = "PM2.5",
                value = "${components.pm2_5.toInt()} μg/m³",
                modifier = Modifier.weight(1f)
            )

            AirQualityDetailItem(
                title = "PM10",
                value = "${components.pm10.toInt()} μg/m³",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            AirQualityDetailItem(
                title = "NO₂",
                value = "${components.no2.toInt()} μg/m³",
                modifier = Modifier.weight(1f)
            )

            AirQualityDetailItem(
                title = "O₃",
                value = "${components.o3.toInt()} μg/m³",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            AirQualityDetailItem(
                title = "SO₂",
                value = "${components.so2.toInt()} μg/m³",
                modifier = Modifier.weight(1f)
            )

            AirQualityDetailItem(
                title = "CO",
                value = "${(components.co / 1000).toInt()} mg/m³",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun AirQualityDetailItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val weatherColors = LocalWeatherColors.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = weatherColors.textSecondary
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = weatherColors.textPrimary,
            fontWeight = FontWeight.Medium
        )
    }
}