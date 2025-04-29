// app/src/main/java/com/example/weatherapp/ui/components/LongTermForecastSection.kt
package com.example.weatherapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.remote.model.ForecastResponse
import com.example.weatherapp.ui.theme.LocalWeatherColors
import com.example.weatherapp.ui.utils.formatDate
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun LongTermForecastSection(
    forecast: ForecastResponse,
    onDayClick: (Long) -> Unit = {}
) {
    // Grupujemy prognozę po dniach
    val dailyForecast = forecast.list.groupBy {
        val date = Date(it.dt * 1000)
        val cal = Calendar.getInstance()
        cal.time = date
        cal.get(Calendar.DAY_OF_YEAR)
    }.values.take(10) // Bierzemy prognozy na 10 dni zamiast 7

    val weatherColors = LocalWeatherColors.current
    val dateFormat = SimpleDateFormat("d MMM", Locale.getDefault())

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
                text = "Długoterminowa prognoza",
                style = MaterialTheme.typography.titleLarge,
                color = weatherColors.textPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(dailyForecast.toList()) { dayForecasts ->
                    val maxTemp = dayForecasts.maxOf { it.main.temp }
                    val minTemp = dayForecasts.minOf { it.main.temp }
                    val mainWeather = dayForecasts
                        .groupBy { it.weather.first().main }
                        .maxByOrNull { it.value.size }
                        ?.value
                        ?.first()
                        ?.weather
                        ?.first()

                    DayForecastItem(
                        date = dateFormat.format(Date(dayForecasts.first().dt * 1000)),
                        dayName = formatDate(dayForecasts.first().dt),
                        maxTemp = maxTemp.toInt(),
                        minTemp = minTemp.toInt(),
                        iconCode = mainWeather?.icon ?: "",
                        onClick = { onDayClick(dayForecasts.first().dt) }
                    )
                }
            }
        }
    }
}

@Composable
fun DayForecastItem(
    date: String,
    dayName: String,
    maxTemp: Int,
    minTemp: Int,
    iconCode: String,
    onClick: () -> Unit = {}
) {
    val weatherColors = LocalWeatherColors.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(70.dp)
            .padding(4.dp)
    ) {
        Text(
            text = dayName,
            style = MaterialTheme.typography.bodyMedium,
            color = weatherColors.textPrimary
        )

        Text(
            text = date,
            style = MaterialTheme.typography.bodySmall,
            color = weatherColors.textSecondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        WeatherIcon(
            iconCode = iconCode,
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "$maxTemp°",
            style = MaterialTheme.typography.bodyLarge,
            color = weatherColors.textPrimary,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "$minTemp°",
            style = MaterialTheme.typography.bodySmall,
            color = weatherColors.textSecondary
        )
    }
}