package com.example.weatherapp.data.remote.model

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

data class AstronomicalData(
    val sunrise: Long,
    val sunset: Long,
    val timezone: Int = 0
) {
    fun getSunriseFormatted(): String {
        return formatTimestamp(sunrise)
    }

    fun getSunsetFormatted(): String {
        return formatTimestamp(sunset)
    }

    fun getDayLength(): String {
        val dayLengthInSeconds = sunset - sunrise
        val hours = (dayLengthInSeconds / 3600).toInt()
        val minutes = ((dayLengthInSeconds % 3600) / 60).toInt()
        return String.format("%02d:%02d", hours, minutes)
    }

    fun getDayProgress(currentTime: Long = System.currentTimeMillis() / 1000): Int {
        val adjustedTime = currentTime + timezone

        if (adjustedTime < sunrise) return 0
        if (adjustedTime > sunset) return 100

        val totalDayLength = sunset - sunrise
        val progress = (adjustedTime - sunrise).toFloat() / totalDayLength * 100
        return progress.roundToInt().coerceIn(0, 100)
    }

    private fun formatTimestamp(timestamp: Long): String {
        val date = Date((timestamp + timezone) * 1000)
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        return format.format(date)
    }

    companion object {
        fun fromWeatherResponse(weatherResponse: WeatherResponse): AstronomicalData {
            return AstronomicalData(
                weatherResponse.sys.sunrise,
                weatherResponse.sys.sunset,
                weatherResponse.timezone
            )
        }
    }
}

data class MoonPhaseData(
    val phase: Double, // 0-1 reprezentujące fazy księżyca (0 i 1 = nów, 0.5 = pełnia)
    val phaseDate: Long
) {
    fun getMoonPhaseName(): String {
        return when {
            phase < 0.025 || phase > 0.975 -> "Nów"
            phase < 0.225 -> "Sierp przybywający"
            phase < 0.275 -> "Pierwsza kwadra"
            phase < 0.475 -> "Przybywający księżyc garbaty"
            phase < 0.525 -> "Pełnia"
            phase < 0.725 -> "Ubywający księżyc garbaty"
            phase < 0.775 -> "Ostatnia kwadra"
            else -> "Sierp ubywający"
        }
    }

    fun getMoonPhaseEmoji(): String {
        return when {
            phase < 0.025 || phase > 0.975 -> "🌑" // Nów
            phase < 0.225 -> "🌒" // Sierp przybywający
            phase < 0.275 -> "🌓" // Pierwsza kwadra
            phase < 0.475 -> "🌔" // Przybywający księżyc garbaty
            phase < 0.525 -> "🌕" // Pełnia
            phase < 0.725 -> "🌖" // Ubywający księżyc garbaty
            phase < 0.775 -> "🌗" // Ostatnia kwadra
            else -> "🌘" // Sierp ubywający
        }
    }
}