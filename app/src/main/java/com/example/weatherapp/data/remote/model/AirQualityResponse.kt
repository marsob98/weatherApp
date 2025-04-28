// app/src/main/java/com/example/weatherapp/data/remote/model/AirQualityResponse.kt
package com.example.weatherapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class AirQualityResponse(
    @SerializedName("coord") val coord: Coord,
    @SerializedName("list") val list: List<AirQualityData>
)

data class AirQualityData(
    @SerializedName("main") val main: AirQualityMain,
    @SerializedName("components") val components: AirQualityComponents,
    @SerializedName("dt") val dt: Long
)

data class AirQualityMain(
    @SerializedName("aqi") val aqi: Int // Air Quality Index (1-5)
)

data class AirQualityComponents(
    @SerializedName("co") val co: Double, // Koncentracja tlenku węgla, μg/m3
    @SerializedName("no") val no: Double, // Koncentracja tlenku azotu, μg/m3
    @SerializedName("no2") val no2: Double, // Koncentracja dwutlenku azotu, μg/m3
    @SerializedName("o3") val o3: Double, // Koncentracja ozonu, μg/m3
    @SerializedName("so2") val so2: Double, // Koncentracja dwutlenku siarki, μg/m3
    @SerializedName("pm2_5") val pm2_5: Double, // Koncentracja PM2.5, μg/m3
    @SerializedName("pm10") val pm10: Double, // Koncentracja PM10, μg/m3
    @SerializedName("nh3") val nh3: Double // Koncentracja amoniaku, μg/m3
)

// Pomocnicza klasa do interpretacji indeksu jakości powietrza
data class AirQualityInfo(
    val level: String,
    val description: String,
    val color: Long,
    val recommendation: String
) {
    companion object {
        fun getAirQualityInfo(aqi: Int): AirQualityInfo {
            return when (aqi) {
                1 -> AirQualityInfo(
                    "Dobra",
                    "Jakość powietrza jest dobra.",
                    0xFF4CAF50, // Zielony
                    "Idealne warunki do aktywności na zewnątrz."
                )
                2 -> AirQualityInfo(
                    "Umiarkowana",
                    "Jakość powietrza jest akceptowalna.",
                    0xFFFFEB3B, // Żółty
                    "Osoby wyjątkowo wrażliwe powinny rozważyć ograniczenie długotrwałego wysiłku na zewnątrz."
                )
                3 -> AirQualityInfo(
                    "Niezdrowa dla wrażliwych",
                    "Jakość powietrza jest niezdrowa dla osób wrażliwych.",
                    0xFFFF9800, // Pomarańczowy
                    "Osoby starsze, dzieci i osoby z chorobami układu oddechowego powinny ograniczyć długotrwały wysiłek na zewnątrz."
                )
                4 -> AirQualityInfo(
                    "Niezdrowa",
                    "Jakość powietrza jest niezdrowa.",
                    0xFFF44336, // Czerwony
                    "Każdy powinien ograniczyć długotrwały wysiłek na zewnątrz. Osoby wrażliwe powinny unikać przebywania na zewnątrz."
                )
                5 -> AirQualityInfo(
                    "Bardzo niezdrowa",
                    "Jakość powietrza jest bardzo niezdrowa.",
                    0xFF9C27B0, // Fioletowy
                    "Wszyscy powinni unikać przebywania na zewnątrz. Osoby wrażliwe powinny pozostać w domu."
                )
                else -> AirQualityInfo(
                    "Niebezpieczna",
                    "Jakość powietrza jest niebezpieczna.",
                    0xFF5D4037, // Brązowy
                    "Wszyscy powinni unikać wysiłku na zewnątrz. Osoby wrażliwe powinny pozostać w domu."
                )
            }
        }
    }
}