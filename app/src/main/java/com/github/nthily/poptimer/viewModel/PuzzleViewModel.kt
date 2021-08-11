package com.github.nthily.poptimer.viewModel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.nthily.poptimer.utils.Puzzles
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.worldcubeassociation.tnoodle.svglite.Svg
import javax.inject.Inject

@HiltViewModel
class PuzzleViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), LifecycleObserver {

    private lateinit var appli: Application

    fun setApp(app: Application) {
        appli = app
    }

    private var scrambleScope = CoroutineScope(Dispatchers.IO)
    var currentType = savedStateHandle.getLiveData("currentType", Puzzles.THREE)

    var scramble: String by mutableStateOf("")
    private var puzzleSvg: Svg? by mutableStateOf(null)
    var puzzleFileLength by mutableStateOf(0L)
    var puzzlePath by mutableStateOf("")

    var bestScore by mutableStateOf(Long.MAX_VALUE)

    var isRefreshingPuzzle by mutableStateOf(false)
    var observePuzzle by mutableStateOf(false)

    fun changeType(type: Puzzles) {
        currentType.value = type
        savedStateHandle["currentType"] = type
        generateScrambleImage()
    }

    fun generateScrambleImage() {
        Log.d(TAG, "value: ${currentType.value}")
        scramble = ""
        puzzleFileLength = 0L
        isRefreshingPuzzle = true
        observePuzzle = false
        scrambleScope.launch {
            val scrambleSteps = currentType.value!!.puzzle.generateScramble()
            val scrambleSvg = currentType.value!!.puzzle.drawScramble(scrambleSteps, hashMapOf())
            val scrambleFile = File(appli.applicationContext.getExternalFilesDir(null), "puzzle.svg")
            scrambleFile.writeText(scrambleSvg.toString())
            delay(500)
            viewModelScope.launch {
                scramble = scrambleSteps
                puzzleSvg = scrambleSvg
                puzzleFileLength = scrambleFile.length()
                puzzlePath = scrambleFile.absolutePath
                isRefreshingPuzzle = false
            }
        }
    }


    init {
        generateScrambleImage()
    }

}