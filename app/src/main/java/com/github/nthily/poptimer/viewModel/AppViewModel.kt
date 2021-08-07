package com.github.nthily.poptimer.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.nthily.poptimer.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.concurrent.timerTask

@HiltViewModel
class AppViewModel @Inject constructor()
    :ViewModel() {

    var selectCube by mutableStateOf(false)
    var bottomNavigationItem by mutableStateOf(1)
    var bottomPadding by mutableStateOf(0.dp)

    /*
     about timer value
    */

    var startTime = 0L
    var time by mutableStateOf(0L)
    var isTiming by mutableStateOf(false)
    var ready by mutableStateOf(false)
    var lastResult by mutableStateOf<Long?>(null)
    private var tempResult: Long? = null


    var scramble by mutableStateOf(Utils.generate3x3x3CubeScramble())


    fun readyStage() {
        tempResult = time
        time = 0L
        ready = true
    }

    fun start() {
        time = 0
        startTime = System.currentTimeMillis()
        isTiming = true
    }

    fun stop() {
        isTiming = false
        lastResult = if(tempResult != 0L) tempResult else null
        ready = false
        scramble = Utils.generate3x3x3CubeScramble()
    }

}