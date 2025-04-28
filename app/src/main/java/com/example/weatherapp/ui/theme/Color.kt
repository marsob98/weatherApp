// app/src/main/java/com/example/weatherapp/ui/theme/Color.kt
package com.example.weatherapp.ui.theme

import androidx.compose.ui.graphics.Color

// Podstawowe kolory - bardziej eleganckie i stonowane
val Primary = Color(0xFF2E3346)
val PrimaryLight = Color(0xFF4A5275)
val PrimaryDark = Color(0xFF1C1B33)

// Akcenty - subtelniejsze i bardziej wyrafinowane
val Accent = Color(0xFF4A90E2)
val AccentLight = Color(0xFF6FB5FF)
val AccentDark = Color(0xFF1E3C72)

// Pogodowe kolory - ulepszone gradienty
val SunnyGradientStart = Color(0xFF4DA0B0)
val SunnyGradientEnd = Color(0xFFD39D38)
val CloudyGradientStart = Color(0xFF757F9A)
val CloudyGradientEnd = Color(0xFF1C2533)
val RainyGradientStart = Color(0xFF616161)
val RainyGradientEnd = Color(0xFF18191A)
val StormGradientStart = Color(0xFF37474F)
val StormGradientEnd = Color(0xFF1C1F27)
val SnowGradientStart = Color(0xFFE8EAF6)
val SnowGradientEnd = Color(0xFF9FA8DA)
val FogGradientStart = Color(0xFFB0BEC5)
val FogGradientEnd = Color(0xFF546E7A)
val NightGradientStart = Color(0xFF172941)
val NightGradientEnd = Color(0xFF000B18)

// Teksty - poprawiona czytelność
val TextPrimary = Color.White
val TextSecondary = Color.White.copy(alpha = 0.7f)
val TextMuted = Color.White.copy(alpha = 0.5f)

// Karty i tła - efekt szkła (glassmorphism)
val CardBackground = Color(0xFF3A3E59).copy(alpha = 0.85f)
val CardBackgroundLight = Color(0xFF4A5275).copy(alpha = 0.5f)
val SurfaceBackground = Color(0xFF2E3346)

// Status - bardziej subtelne
val Error = Color(0xFFEF5350)
val Success = Color(0xFF66BB6A)
val Warning = Color(0xFFFFCA28)
val Info = Color(0xFF42A5F5)

// Ikony - bardziej wyraziste
val HeartActive = Color(0xFFFF4081)
val HeartInactive = Color.White.copy(alpha = 0.7f)

// Nowe kolory dla glassmorphism
val GlassLight = Color.White.copy(alpha = 0.1f)
val GlassBorder = Color.White.copy(alpha = 0.2f)
val GlassShadow = Color.Black.copy(alpha = 0.1f)