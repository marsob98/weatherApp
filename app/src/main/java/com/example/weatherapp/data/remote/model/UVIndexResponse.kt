// Plik: app/src/main/java/com/example/weatherapp/data/remote/model/UVIndexResponse.kt
package com.example.weatherapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class UVIndexResponse(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
    @SerializedName("date_iso") val dateIso: String,
    @SerializedName("date") val date: Long,
    @SerializedName("value") val value: Double
)

// Klasa z zaleceniami w zależności od indeksu UV
data class UVRecommendation(
    val level: String,
    val description: String,
    val recommendation: String
) {
    companion object {
        fun getRecommendation(uvIndex: Double): UVRecommendation {
            return when {
                uvIndex < 3 -> UVRecommendation(
                    "Niski",
                    "Niskie zagrożenie z promieniowania UV dla przeciętnej osoby.",
                    "Możesz bezpiecznie przebywać na zewnątrz."
                )
                uvIndex < 6 -> UVRecommendation(
                    "Umiarkowany",
                    "Umiarkowane ryzyko uszkodzenia skóry z powodu UV.",
                    "Załóż okulary przeciwsłoneczne i używaj kremu z filtrem SPF 30+."
                )
                uvIndex < 8 -> UVRecommendation(
                    "Wysoki",
                    "Wysokie ryzyko uszkodzenia skóry z powodu UV.",
                    "Ogranicz przebywanie na słońcu w godzinach 10-16. Używaj kremu z filtrem SPF 30+, noś koszulę i kapelusz."
                )
                uvIndex < 11 -> UVRecommendation(
                    "Bardzo wysoki",
                    "Bardzo wysokie ryzyko uszkodzenia skóry.",
                    "Unikaj przebywania na słońcu w godzinach 10-16. Używaj kremu z filtrem SPF 50+, noś koszulę z długim rękawem, kapelusz i okulary przeciwsłoneczne."
                )
                else -> UVRecommendation(
                    "Ekstremalny",
                    "Ekstremalne ryzyko uszkodzenia skóry.",
                    "Pozostań w cieniu. Używaj kremu z filtrem SPF 50+, noś koszulę z długim rękawem, kapelusz i okulary przeciwsłoneczne."
                )
            }
        }
    }
}