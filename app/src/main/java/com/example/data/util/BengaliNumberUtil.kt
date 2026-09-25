package com.example.data.util

object BengaliNumberUtil {
    private val bengaliDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')

    fun toBengaliNumber(number: Int): String {
        return number.toString().map { char ->
            if (char in '0'..'9') {
                bengaliDigits[char - '0']
            } else {
                char
            }
        }.joinToString("")
    }

    fun toBengaliNumber(numberStr: String): String {
        return numberStr.map { char ->
            if (char in '0'..'9') {
                bengaliDigits[char - '0']
            } else {
                char
            }
        }.joinToString("")
    }

    fun formatDuration(ms: Int): String {
        val totalSecs = (ms / 1000).coerceAtLeast(0)
        val minutes = totalSecs / 60
        val seconds = totalSecs % 60
        return "%02d:%02d".format(minutes, seconds)
    }

    fun formatDurationBengali(ms: Int): String {
        return toBengaliNumber(formatDuration(ms))
    }
}
