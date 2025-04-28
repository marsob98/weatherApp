// app/src/main/java/com/example/weatherapp/ui/theme/Theme.kt
package com.example.weatherapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Accent,
    secondary = AccentLight,
    tertiary = AccentDark,
    background = PrimaryDark,
    surface = SurfaceBackground,
    error = Error,
    onPrimary = TextPrimary,
    onSecondary = TextPrimary,
    onTertiary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onError = TextPrimary
)

private val LightColorScheme = lightColorScheme(
    primary = Accent,
    secondary = AccentLight,
    tertiary = AccentDark,
    background = Color(0xFFF5F5F5),
    surface = Color.White,
    error = Error,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B33),
    onSurface = Color(0xFF1C1B33),
    onError = Color.White
)

// Dodatkowe kolory dla customowego motywu
data class WeatherColors(
    val textPrimary: Color = TextPrimary,
    val cardBackground: Color = CardBackground,
    val cardBackgroundLight: Color = CardBackgroundLight,
    val textSecondary: Color = TextSecondary,
    val textMuted: Color = TextMuted,
    val heartActive: Color = HeartActive,
    val heartInactive: Color = HeartInactive,
    val sunnyGradient: List<Color> = listOf(SunnyGradientStart, SunnyGradientEnd),
    val cloudyGradient: List<Color> = listOf(CloudyGradientStart, CloudyGradientEnd),
    val rainyGradient: List<Color> = listOf(RainyGradientStart, RainyGradientEnd),
    val stormGradient: List<Color> = listOf(StormGradientStart, StormGradientEnd),
    val snowGradient: List<Color> = listOf(SnowGradientStart, SnowGradientEnd),
    val fogGradient: List<Color> = listOf(FogGradientStart, FogGradientEnd),
    val nightGradient: List<Color> = listOf(NightGradientStart, NightGradientEnd),
    val defaultGradient: List<Color> = listOf(PrimaryLight, PrimaryDark)
)

val LocalWeatherColors = staticCompositionLocalOf { WeatherColors() }

@Composable
fun WeatherAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val weatherColors = WeatherColors()

    CompositionLocalProvider(
        LocalWeatherColors provides weatherColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}