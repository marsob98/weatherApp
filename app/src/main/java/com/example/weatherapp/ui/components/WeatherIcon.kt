package com.example.weatherapp.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WeatherIcon(iconCode: String, modifier: Modifier = Modifier, tint: Color = Color.White) {
    val infiniteTransition = rememberInfiniteTransition()
    val sunScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val cloudTranslation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val rainRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val (symbol, backgroundColor, animModifier) = when {
        iconCode.contains("01") -> Triple(
            "☀️",
            Color(0xFFFFA000),
            Modifier.scale(if (iconCode.contains("d")) sunScale else 1f)
        )
        iconCode.contains("02") || iconCode.contains("03") -> Triple(
            "⛅",
            Color(0xFF78909C),
            Modifier.rotate(cloudTranslation)
        )
        iconCode.contains("04") -> Triple(
            "☁️",
            Color(0xFF546E7A),
            Modifier.rotate(cloudTranslation)
        )
        iconCode.contains("09") || iconCode.contains("10") -> Triple(
            "🌧️",
            Color(0xFF42A5F5),
            Modifier.rotate(rainRotation)
        )
        iconCode.contains("11") -> Triple(
            "⚡",
            Color(0xFF5C6BC0),
            Modifier.scale(sunScale)
        )
        iconCode.contains("13") -> Triple(
            "❄️",
            Color(0xFFB3E5FC),
            Modifier.rotate(rainRotation)
        )
        iconCode.contains("50") -> Triple(
            "🌫️",
            Color(0xFF90A4AE),
            Modifier.scale(1f)
        )
        else -> Triple(
            "🌤️",
            Color(0xFF8D6E63),
            Modifier.scale(1f)
        )
    }

    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(backgroundColor.copy(alpha = 0.7f))
            .then(animModifier),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = symbol,
            fontSize = 18.sp,
            color = tint,
            textAlign = TextAlign.Center
        )
    }
}