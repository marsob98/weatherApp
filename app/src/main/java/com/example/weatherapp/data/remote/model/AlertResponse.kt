// app/src/main/java/com/example/weatherapp/data/remote/model/AlertResponse.kt
package com.example.weatherapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class AlertResponse(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
    @SerializedName("timezone") val timezone: String,
    @SerializedName("timezone_offset") val timezoneOffset: Int,
    @SerializedName("alerts") val alerts: List<Alert>
)

data class Alert(
    @SerializedName("sender_name") val senderName: String,
    @SerializedName("event") val event: String,
    @SerializedName("start") val start: Long,
    @SerializedName("end") val end: Long,
    @SerializedName("description") val description: String,
    @SerializedName("tags") val tags: List<String>
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