package com.github.nthily.poptimer.utils

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import java.time.ZonedDateTime
import java.time.chrono.IsoChronology
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.math.min

object Utils {

    @JvmStatic
    fun solveTimeFormat(time: Long):String {
        return String.format(
            when (time) {
                in 0 until 60 * 1000 -> "%1\$tS.%1\$tL"
                in 60 * 1000 until 60 * 60 * 1000 -> "%1\$tM:%1\$tS.%1\$tL"
                else -> "%2\$d:%1\$tM:%1\$tS.%1\$tL"
            },
            time, TimeUnit.MILLISECONDS.toHours(time)
        ).replace(Regex("(^0)|(\\d$)"), "") // removing numbers at the beginning and end
    }

    fun log(str: String) {
        Log.d(TAG, str)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun monthDayFormat(timestamp: ZonedDateTime, style: FormatStyle): String {
        val pattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(style, null, IsoChronology.INSTANCE, Locale.getDefault())
        val dayRange = "d+[^,./-]?".toRegex().find(pattern)!!.range
        val monthRange = "[ML]+[^,./-]?".toRegex().find(pattern)!!.range
        val startIdx = min(dayRange.first, monthRange.first)
        val endIdx = max(dayRange.last, monthRange.last)
        val remaining = pattern.substring(startIdx, endIdx + 1)
        return timestamp.format(DateTimeFormatter.ofPattern(remaining))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun timeFormat(timestamp: ZonedDateTime, context: Context): String {
        var pattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(null, FormatStyle.SHORT, IsoChronology.INSTANCE, Locale.getDefault())
        val is24Hours = Settings.System.getInt(context.contentResolver, Settings.System.TIME_12_24, 0) == 24
        if(is24Hours) pattern = pattern.replace(Regex("[hKk]+"), "H").replace("a", "").trim()
        return timestamp.format(DateTimeFormatter.ofPattern(pattern))
    }
}
