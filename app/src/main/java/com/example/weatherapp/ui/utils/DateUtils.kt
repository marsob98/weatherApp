package com.example.weatherapp.ui.utils

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