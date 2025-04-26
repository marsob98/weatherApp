package com.example.weatherapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WeatherIcon(iconCode: String, modifier: Modifier = Modifier, tint: Color = Color.White) {
    // Zamiast używać ikon, użyjmy prostego tekstu w kolorowym kółku
    val (symbol, backgroundColor) = when {
        // Słońce
        iconCode.contains("01") -> "☀️" to Color(0xFFFFA000)
        // Częściowe zachmurzenie
        iconCode.contains("02") || iconCode.contains("03") -> "⛅" to Color(0xFF78909C)
        // Zachmurzenie
        iconCode.contains("04") -> "☁️" to Color(0xFF546E7A)
        // Deszcz
        iconCode.contains("09") || iconCode.contains("10") -> "🌧️" to Color(0xFF42A5F5)
        // Burza
        iconCode.contains("11") -> "⚡" to Color(0xFF5C6BC0)
        // Śnieg
        iconCode.contains("13") -> "❄️" to Color(0xFFB3E5FC)
        // Mgła
        iconCode.contains("50") -> "🌫️" to Color(0xFF90A4AE)
        // Domyślnie
        else -> "🌤️" to Color(0xFF8D6E63)
    }

    Box(
        modifier = modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(backgroundColor.copy(alpha = 0.7f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = symbol,
            fontSize = 16.sp,
            color = tint,
            textAlign = TextAlign.Center
        )
    }
}