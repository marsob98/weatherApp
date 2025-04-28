package com.example.weatherapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.data.remote.model.ForecastResponse
import com.example.weatherapp.ui.utils.formatDate
import java.util.*

@Composable
fun DailyForecastSection(forecast: ForecastResponse) {
    // Grupujemy prognozę po dniach
    val dailyForecast = forecast.list.groupBy {
        val date = Date(it.dt * 1000)
        val cal = Calendar.getInstance()
        cal.time = date
        cal.get(Calendar.DAY_OF_YEAR)
    }.values.take(7) // Bierzemy tylko 7 dni

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color(0xFF3A3E59).copy(alpha = 0.7f),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Prognoza tygodniowa",
                fontSize = 18.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            dailyForecast.forEach { dayForecasts ->
                val maxTemp = dayForecasts.maxOf { it.main.temp }
                val minTemp = dayForecasts.minOf { it.main.temp }
                val weather = dayForecasts.first().weather.first()

                DailyForecastItem(
                    day = formatDate(dayForecasts.first().dt),
                    maxTemp = maxTemp.toInt(),
                    minTemp = minTemp.toInt(),
                    weatherDescription = weather.description,
                    iconCode = weather.icon
                )

                Spacer(modifier = Modifier.height(8.dp))
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
    iconCode: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = day,
            fontSize = 16.sp,
            color = Color.White,
            modifier = Modifier.width(60.dp)
        )

        // Dodajemy ikonę pogody
        WeatherIcon(iconCode = iconCode)

        Text(
            text = weatherDescription.capitalize(),
            fontSize = 16.sp,
            color = Color.White,
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
        )

        Text(
            text = "$minTemp° - $maxTemp°",
            fontSize = 16.sp,
            color = Color.White,
            modifier = Modifier.width(80.dp)
        )
    }
}