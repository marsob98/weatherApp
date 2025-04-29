// app/src/main/java/com/example/weatherapp/data/remote/model/ForecastExtensions.kt
package com.example.weatherapp.data.remote.model

import java.util.*

/**
 * Rozszerzenia dla klasy ForecastItem
 */

/**
 * Filtruje listę prognoz dla konkretnego dnia
 * @param date Timestamp (w sekundach) dnia, dla którego chcemy filtry
 * @return Lista prognoz dla danego dnia
 */
fun List<ForecastItem>.filterForDay(date: Long): List<ForecastItem> {
    val calendar = Calendar.getInstance().apply { timeInMillis = date * 1000 }

    return this.filter {
        val forecastCal = Calendar.getInstance().apply { timeInMillis = it.dt * 1000 }

        forecastCal.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR) &&
                forecastCal.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
    }
}

/**
 * Oblicza średnią temperaturę dla dnia
 * @return Średnia temperatura jako Double
 */
fun List<ForecastItem>.getAverageTemperature(): Double {
    if (this.isEmpty()) return 0.0
    return this.map { it.main.temp }.average()
}

/**
 * Oblicza minimalną temperaturę dla dnia
 * @return Minimalna temperatura jako Double
 */
fun List<ForecastItem>.getMinTemperature(): Double {
    if (this.isEmpty()) return 0.0
    return this.minOf { it.main.temp }
}

/**
 * Oblicza maksymalną temperaturę dla dnia
 * @return Maksymalna temperatura jako Double
 */
fun List<ForecastItem>.getMaxTemperature(): Double {
    if (this.isEmpty()) return 0.0
    return this.maxOf { it.main.temp }
}

/**
 * Oblicza średnią wilgotność dla dnia
 * @return Średnia wilgotność jako Int
 */
fun List<ForecastItem>.getAverageHumidity(): Int {
    if (this.isEmpty()) return 0
    return this.map { it.main.humidity }.average().toInt()
}

/**
 * Oblicza średnią prędkość wiatru dla dnia
 * @return Średnia prędkość wiatru jako Double
 */
fun List<ForecastItem>.getAverageWindSpeed(): Double {
    if (this.isEmpty()) return 0.0
    return this.map { it.wind.speed }.average()
}

/**
 * Pobiera najczęściej występującą pogodę dla dnia
 * @return Obiekt Weather lub null jeśli lista jest pusta
 */
fun List<ForecastItem>.getMostFrequentWeather(): Weather? {
    if (this.isEmpty()) return null

    return this
        .flatMap { it.weather }
        .groupBy { it.main }
        .maxByOrNull { it.value.size }
        ?.value
        ?.firstOrNull()
}

/**
 * Sprawdza, czy prognoza zawiera opady deszczu
 * @return true jeśli jest deszcz, false w przeciwnym razie
 */
fun List<ForecastItem>.hasRain(): Boolean {
    if (this.isEmpty()) return false

    return this
        .flatMap { it.weather }
        .any { it.main.equals("Rain", ignoreCase = true) }
}

/**
 * Sprawdza, czy prognoza zawiera opady śniegu
 * @return true jeśli jest śnieg, false w przeciwnym razie
 */
fun List<ForecastItem>.hasSnow(): Boolean {
    if (this.isEmpty()) return false

    return this
        .flatMap { it.weather }
        .any { it.main.equals("Snow", ignoreCase = true) }
}

/**
 * Oblicza procent zachmurzenia dla dnia
 * @return Średni procent zachmurzenia jako Int
 */
fun List<ForecastItem>.getAverageCloudiness(): Int {
    if (this.isEmpty()) return 0
    return this.map { it.clouds.all }.average().toInt()
}

/**
 * Pobiera nazwę dnia tygodnia dla pierwszej prognozy
 * @return Nazwa dnia tygodnia lub pusty string jeśli lista jest pusta
 */
fun List<ForecastItem>.getDayName(): String {
    if (this.isEmpty()) return ""

    val calendar = Calendar.getInstance().apply { timeInMillis = this@getDayName.first().dt * 1000 }

    return when (calendar.get(Calendar.DAY_OF_WEEK)) {
        Calendar.MONDAY -> "Poniedziałek"
        Calendar.TUESDAY -> "Wtorek"
        Calendar.WEDNESDAY -> "Środa"
        Calendar.THURSDAY -> "Czwartek"
        Calendar.FRIDAY -> "Piątek"
        Calendar.SATURDAY -> "Sobota"
        Calendar.SUNDAY -> "Niedziela"
        else -> ""
    }
}

/**
 * Tworzy obiekt PrecipitationInfo na podstawie prognoz dla dnia
 * @return Obiekt PrecipitationInfo lub null jeśli nie ma opadów
 */
fun List<ForecastItem>.createPrecipitationInfo(): PrecipitationInfo? {
    // Jeśli lista jest pusta lub nie ma deszczu/śniegu, zwróć null
    if (this.isEmpty() || (!this.hasRain() && !this.hasSnow())) return null

    // Ustal typ opadów (deszcz lub śnieg)
    val precipitationType = if (this.hasRain()) "Deszcz" else "Śnieg"

    // Oblicz średnią ilość opadów (przykładowa implementacja)
    // W rzeczywistym API, będziesz musiał dostosować to do struktury danych API
    val averageAmount = 1.5 // Przykładowa wartość

    // Określ intensywność opadów
    val intensity = when {
        averageAmount < 0.5 -> "Lekkie opady"
        averageAmount < 4.0 -> "Umiarkowane opady"
        averageAmount < 8.0 -> "Intensywne opady"
        else -> "Bardzo intensywne opady"
    }

    return PrecipitationInfo(
        amount = averageAmount,
        intensity = intensity,
        type = precipitationType,
        period = "24h"
    )
}