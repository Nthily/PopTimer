package com.github.nthily.poptimer.utils

import androidx.core.graphics.scaleMatrix
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

    fun generate3x3x3CubeScramble(): String {
        val suffix = arrayOf("\'", "2")
        val layer = arrayOf("F", "R", "L", "B", "U", "D")
        var temp = ""
        val scramble = mutableListOf<String>()
        val scrambleLength = 30
        scramble.add(getOneStep(layer, suffix))

        for(index in 1 until scrambleLength) {
            do {
                temp = getOneStep(layer, suffix)
            } while (temp[0] == scramble[index - 1][0])
            scramble.add(temp)
        }
        return scramble
            .toString()
            .replace("[", "")
            .replace("]", "")
            .replace(",", "")
    }

    private fun getOneStep(layer: Array<String>, suffix: Array<String>):String {
        return layer.random() + suffix.random() + " "
    }
}