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

@Composable
fun WeatherBackground(
    weather: Weather?,
    content: @Composable () -> Unit
) {
    val backgroundBrush = remember(weather) {
        when (weather?.main?.lowercase()) {
            "clear" -> Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF4A90E2),
                    Color(0xFF1E3C72)
                )
            )
            "clouds" -> Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF757F9A),
                    Color(0xFF1C2533)
                )
            )
            "rain", "drizzle" -> Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF616161),
                    Color(0xFF18191A)
                )
            )
            "thunderstorm" -> Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF37474F),
                    Color(0xFF1C1F27)
                )
            )
            "snow" -> Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFA3A9B2),
                    Color(0xFF596164)
                )
            )
            "fog", "mist" -> Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF9E9E9E),
                    Color(0xFF424242)
                )
            )
            else -> Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF2E3346),
                    Color(0xFF1C1B33)
                )
            )
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