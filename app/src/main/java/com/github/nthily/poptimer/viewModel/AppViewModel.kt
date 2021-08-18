package com.github.nthily.poptimer.viewModel

import android.app.Application
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.github.nthily.poptimer.database.Puzzle
import com.github.nthily.poptimer.database.PuzzleDatabase
import com.github.nthily.poptimer.database.PuzzleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    application: Application,
    private val puzzleRepository: PuzzleRepository
) :AndroidViewModel(application) {
    
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

    /*
     about Database value
    */
    var all = puzzleRepository.all

    fun readyStage() {
        tempResult = time
        time = 0L
        ready = true
    }

    fun start() {
        startTime = System.currentTimeMillis()
        isTiming = true
    }

    fun stop(puzzleViewModel: PuzzleViewModel) {
        viewModelScope.launch {
            isTiming = false
            lastResult = if(tempResult != 0L) tempResult else null
            ready = false
            if(time < puzzleViewModel.bestScore) {
                puzzleViewModel.bestScore = time
            }
            withContext(Dispatchers.IO) {
                puzzleRepository.puzzleDao.insert(Puzzle(0, time, 5L, puzzleViewModel.currentType))
            }
        }
    }
}