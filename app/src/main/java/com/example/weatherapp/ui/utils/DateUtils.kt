// app/src/main/java/com/example/weatherapp/ui/utils/DateUtils.kt

package com.example.weatherapp.ui.utils

import android.content.Context
import com.example.weatherapp.R
import java.text.SimpleDateFormat
import java.util.*

fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp * 1000)
    val format = SimpleDateFormat("HH:mm", Locale("pl", "PL"))
    return format.format(date)
}

fun formatDate(timestamp: Long): String {
    val date = Date(timestamp * 1000)
    val format = SimpleDateFormat("EEEE", Locale("pl", "PL"))
    // Pierwsza litera jest zamieniana na wielką
    return format.format(date).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

// Dodajemy nową metodę dla skróconych nazw dni tygodnia
fun formatDateShortName(timestamp: Long, context: Context): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp * 1000

    return when (calendar.get(Calendar.DAY_OF_WEEK)) {
        Calendar.MONDAY -> context.getString(R.string.monday_short)
        Calendar.TUESDAY -> context.getString(R.string.tuesday_short)
        Calendar.WEDNESDAY -> context.getString(R.string.wednesday_short)
        Calendar.THURSDAY -> context.getString(R.string.thursday_short)
        Calendar.FRIDAY -> context.getString(R.string.friday_short)
        Calendar.SATURDAY -> context.getString(R.string.saturday_short)
        Calendar.SUNDAY -> context.getString(R.string.sunday_short)
        else -> ""
    }
}

fun formatFullDate(timestamp: Long): String {
    val date = Date(timestamp * 1000)
    val format = SimpleDateFormat("d MMMM yyyy", Locale("pl", "PL"))
    return format.format(date)
}

fun formatDateShort(timestamp: Long): String {
    val date = Date(timestamp * 1000)
    val format = SimpleDateFormat("d MMM", Locale("pl", "PL"))
    return format.format(date)
}

fun formatDateTime(timestamp: Long): String {
    val date = Date(timestamp * 1000)
    val format = SimpleDateFormat("HH:mm", Locale("pl", "PL"))
    return format.format(date)
}