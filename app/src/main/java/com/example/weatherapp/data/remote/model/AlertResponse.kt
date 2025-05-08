package com.example.weatherapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class AlertResponse(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
    @SerializedName("timezone") val timezone: String,
    @SerializedName("timezone_offset") val timezoneOffset: Int,
    @SerializedName("alerts") val alerts: List<Alert> = emptyList(),
    @SerializedName("current") val current: CurrentWeather? = null,
    @SerializedName("daily") val daily: List<DailyForecast>? = null
)

data class Alert(
    @SerializedName("sender_name") val senderName: String,
    @SerializedName("event") val event: String,
    @SerializedName("start") val start: Long,
    @SerializedName("end") val end: Long,
    @SerializedName("description") val description: String,
    @SerializedName("tags") val tags: List<String> = emptyList()
) {
    fun getAlertSeverity(): AlertSeverity {
        return when {
            event.contains("warning", ignoreCase = true) -> AlertSeverity.WARNING
            event.contains("watch", ignoreCase = true) -> AlertSeverity.WATCH
            event.contains("advisory", ignoreCase = true) -> AlertSeverity.ADVISORY
            else -> AlertSeverity.INFORMATION
        }
    }
}

enum class AlertSeverity(val color: Long) {
    WARNING(0xFFF44336), // Czerwony
    WATCH(0xFFFF9800),   // Pomarańczowy
    ADVISORY(0xFFFFEB3B), // Żółty
    INFORMATION(0xFF2196F3) // Niebieski
}

data class CurrentWeather(
    @SerializedName("dt") val dt: Long,
    @SerializedName("sunrise") val sunrise: Long,
    @SerializedName("sunset") val sunset: Long,
    @SerializedName("temp") val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("dew_point") val dewPoint: Double,
    @SerializedName("uvi") val uvi: Double,
    @SerializedName("clouds") val clouds: Int,
    @SerializedName("visibility") val visibility: Int,
    @SerializedName("wind_speed") val windSpeed: Double,
    @SerializedName("wind_deg") val windDeg: Int,
    @SerializedName("weather") val weather: List<Weather>
)

data class DailyForecast(
    @SerializedName("dt") val dt: Long,
    @SerializedName("sunrise") val sunrise: Long,
    @SerializedName("sunset") val sunset: Long,
    @SerializedName("moonrise") val moonrise: Long,
    @SerializedName("moonset") val moonset: Long,
    @SerializedName("moon_phase") val moonPhase: Double,
    @SerializedName("temp") val temp: DailyTemp,
    @SerializedName("feels_like") val feelsLike: DailyFeelsLike,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("dew_point") val dewPoint: Double,
    @SerializedName("wind_speed") val windSpeed: Double,
    @SerializedName("wind_deg") val windDeg: Int,
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("clouds") val clouds: Int,
    @SerializedName("pop") val pop: Double,
    @SerializedName("rain") val rain: Double? = null,
    @SerializedName("snow") val snow: Double? = null,
    @SerializedName("uvi") val uvi: Double
)

data class DailyTemp(
    @SerializedName("day") val day: Double,
    @SerializedName("min") val min: Double,
    @SerializedName("max") val max: Double,
    @SerializedName("night") val night: Double,
    @SerializedName("eve") val eve: Double,
    @SerializedName("morn") val morn: Double
)

data class DailyFeelsLike(
    @SerializedName("day") val day: Double,
    @SerializedName("night") val night: Double,
    @SerializedName("eve") val eve: Double,
    @SerializedName("morn") val morn: Double
)