package com.github.nthily.poptimer.utils

import android.provider.Settings
import android.util.Log
import java.util.*
import kotlin.system.measureTimeMillis

class StopWatch: Timer() {
    @Override
    fun start() {
        val timer = Timer().schedule(object : TimerTask() {
            override fun run() {
                Log.e("NIlu_TAG","Hello World")
            }
        }, 0)

    }
}