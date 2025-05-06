// app/src/main/java/com/example/weatherapp/ui/components/DailyForecastSection.kt

package com.example.weatherapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.remote.model.ForecastResponse
import com.example.weatherapp.ui.theme.LocalWeatherColors
import com.example.weatherapp.ui.utils.formatDateShortName
import java.util.*

@Composable
fun DailyForecastSection(
    forecast: ForecastResponse,
    onDayClick: (Long) -> Unit = {}
) {
    // Grupujemy prognozę po dniach
    val dailyForecast = forecast.list.groupBy {
        val date = Date(it.dt * 1000)
        val cal = Calendar.getInstance()
        cal.time = date
        cal.get(Calendar.DAY_OF_YEAR)
    }.values.take(7) // Bierzemy tylko 7 dni

    val weatherColors = LocalWeatherColors.current
    val context = LocalContext.current

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
                text = "Prognoza tygodniowa",
                style = MaterialTheme.typography.titleLarge,
                color = weatherColors.textPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            dailyForecast.forEach { dayForecasts ->
                val maxTemp = dayForecasts.maxOf { it.main.temp }
                val minTemp = dayForecasts.minOf { it.main.temp }
                val weather = dayForecasts.first().weather.first()

                DailyForecastItem(
                    day = formatDateShortName(dayForecasts.first().dt, context),
                    maxTemp = maxTemp.toInt(),
                    minTemp = minTemp.toInt(),
                    weatherDescription = weather.description,
                    iconCode = weather.icon,
                    onClick = { onDayClick(dayForecasts.first().dt) }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun DailyForecastItem(
    day: String,
    maxTemp: Int,
    minTemp: Int,
    weatherDescription: String,
    iconCode: String,
    onClick: () -> Unit = {}
) {
    val weatherColors = LocalWeatherColors.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = day,
            style = MaterialTheme.typography.bodyLarge,
            color = weatherColors.textPrimary,
            modifier = Modifier.width(60.dp)
        )

        // Dodajemy animowaną ikonę pogody
        WeatherIcon(iconCode = iconCode)

        Text(
            text = weatherDescription.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                else it.toString()
            },
            style = MaterialTheme.typography.bodyMedium,
            color = weatherColors.textPrimary,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )

        Text(
            text = "$minTemp° - $maxTemp°",
            style = MaterialTheme.typography.bodyLarge,
            color = weatherColors.textPrimary,
            modifier = Modifier.width(80.dp)
        )
    }
}