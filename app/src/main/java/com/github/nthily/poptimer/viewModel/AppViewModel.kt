package com.github.nthily.poptimer.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.concurrent.timerTask

@HiltViewModel
class AppViewModel @Inject constructor()
    :ViewModel() {

    var selectCube by mutableStateOf(false)
    var bottomNavigationItem by mutableStateOf(1)

    var time by mutableStateOf(0L)
    var timer = Timer()
    var isTiming by mutableStateOf(false)

    fun startTimer() {
        time = 0L
        isTiming = true
        timer.scheduleAtFixedRate(
            timerTask {
                viewModelScope.launch {
                    time = time.plus(1)
                }
            }, 0, 1
        )
    }

    fun stopTimer() {
        isTiming = false
        timer.cancel()
        timer = Timer()
    }
}