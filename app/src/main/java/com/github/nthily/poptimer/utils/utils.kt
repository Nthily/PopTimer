package com.github.nthily.poptimer.utils

import java.util.concurrent.TimeUnit

object Utils {
    @JvmStatic
    fun format(time: Long):String {
        return String.format(
            when (time) {
                in 0 until 60 * 1000 -> "%1\$tS.%1\$tL"
                in 60 * 1000 until 60 * 60 * 1000 -> "%1\$tM:%1\$tS.%1\$tL"
                else -> "%2\$d:%1\$tM:%1\$tS.%1\$tL"
            },
            time, TimeUnit.MILLISECONDS.toHours(time)
        ).replace(Regex("(^0)|(\\d$)"), "") // removing numbers at the beginning and end
    }
}