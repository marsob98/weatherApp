package com.example.weatherapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.weatherapp.data.remote.model.Weather
import com.example.weatherapp.ui.theme.LocalWeatherColors
import java.time.LocalTime

@Composable
fun WeatherBackground(
    weather: Weather?,
    content: @Composable () -> Unit
) {
    val weatherColors = LocalWeatherColors.current
    val currentHour = LocalTime.now().hour
    val isNight = currentHour < 6 || currentHour > 19

    val backgroundBrush = remember(weather, isNight) {
        when {
            isNight -> Brush.verticalGradient(weatherColors.nightGradient)
            weather?.main?.lowercase() == "clear" -> Brush.verticalGradient(weatherColors.sunnyGradient)
            weather?.main?.lowercase() == "clouds" -> Brush.verticalGradient(weatherColors.cloudyGradient)
            weather?.main?.lowercase() in listOf("rain", "drizzle") -> Brush.verticalGradient(weatherColors.rainyGradient)
            weather?.main?.lowercase() == "thunderstorm" -> Brush.verticalGradient(weatherColors.stormGradient)
            weather?.main?.lowercase() == "snow" -> Brush.verticalGradient(weatherColors.snowGradient)
            weather?.main?.lowercase() in listOf("fog", "mist") -> Brush.verticalGradient(weatherColors.fogGradient)
            else -> Brush.verticalGradient(weatherColors.defaultGradient)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundBrush)
    ) {
        content()
    }
}