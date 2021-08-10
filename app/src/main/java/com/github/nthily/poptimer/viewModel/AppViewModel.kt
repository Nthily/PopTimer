package com.github.nthily.poptimer.viewModel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.nthily.poptimer.utils.NbyNCubePuzzle
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.worldcubeassociation.tnoodle.svglite.Svg
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(application: Application)
    :AndroidViewModel(application) {

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
    var isRefreshingPuzzle by mutableStateOf(false)
    var observePuzzle by mutableStateOf(false)

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
    }
}