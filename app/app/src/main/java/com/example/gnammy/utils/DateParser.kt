package com.example.gnammy.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

enum class DateFormats(val pattern: String) {
    DB_FORMAT("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
    SHOW_FORMAT("dd/MM/yyyy")
}

fun dateStringToMillis(dateString: String, format: DateFormats): Long {
    val sdf = SimpleDateFormat(format.pattern, Locale.getDefault())
    if (format == DateFormats.DB_FORMAT) {
        sdf.timeZone = TimeZone.getTimeZone("Europe/Rome")
    }
    return sdf.parse(dateString)?.time ?: throw IllegalArgumentException("Data non valida: $dateString")
}

fun millisToDateString(millis: Long, format: DateFormats): String {
    val sdf = SimpleDateFormat(format.pattern, Locale.getDefault())
    if (format == DateFormats.DB_FORMAT) {
        sdf.timeZone = TimeZone.getTimeZone("Europe/Rome")
    }
    return sdf.format(millis)
}