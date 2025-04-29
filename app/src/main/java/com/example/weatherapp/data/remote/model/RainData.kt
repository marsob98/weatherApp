// Plik: app/src/main/java/com/example/weatherapp/data/remote/model/RainData.kt
package com.example.weatherapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class RainData(
    @SerializedName("1h") val oneHour: Double? = null,
    @SerializedName("3h") val threeHours: Double? = null
)

data class SnowData(
    @SerializedName("1h") val oneHour: Double? = null,
    @SerializedName("3h") val threeHours: Double? = null
)

data class PrecipitationInfo(
    val amount: Double,
    val intensity: String,
    val type: String,
    val period: String
) {
    companion object {
        fun fromRainData(rainData: RainData?): PrecipitationInfo? {
            if (rainData == null) return null

            val amount = rainData.oneHour ?: rainData.threeHours ?: return null
            val period = if (rainData.oneHour != null) "1h" else "3h"

            val intensity = when {
                amount < 0.5 -> "Lekkie opady"
                amount < 4.0 -> "Umiarkowane opady"
                amount < 8.0 -> "Intensywne opady"
                else -> "Bardzo intensywne opady"
            }

            return PrecipitationInfo(amount, intensity, "Deszcz", period)
        }

        fun fromSnowData(snowData: SnowData?): PrecipitationInfo? {
            if (snowData == null) return null

            val amount = snowData.oneHour ?: snowData.threeHours ?: return null
            val period = if (snowData.oneHour != null) "1h" else "3h"

            val intensity = when {
                amount < 0.5 -> "Lekkie opady"
                amount < 4.0 -> "Umiarkowane opady"
                amount < 8.0 -> "Intensywne opady"
                else -> "Bardzo intensywne opady"
            }

            return PrecipitationInfo(amount, intensity, "Śnieg", period)
        }
    }
}