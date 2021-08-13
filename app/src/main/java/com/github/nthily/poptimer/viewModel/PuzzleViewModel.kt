package com.github.nthily.poptimer.viewModel

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.nthily.poptimer.utils.Puzzles
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.worldcubeassociation.tnoodle.svglite.Svg
import javax.inject.Inject

@HiltViewModel
class PuzzleViewModel @Inject constructor(
    @ApplicationContext private val app: Context
) : ViewModel() {

    private val config = app.getSharedPreferences("puzzle.config", MODE_PRIVATE)
    private var scrambleScope = CoroutineScope(Dispatchers.IO)
    private var puzzleSvg: Svg? by mutableStateOf(null)

    var bestScore by mutableStateOf(Long.MAX_VALUE)
    var currentType by mutableStateOf(Puzzles.valueOf(config.getString("currentType", "THREE") ?: "THREE"))
    var observePuzzle by mutableStateOf(false)
    var puzzlePath by mutableStateOf("")
    var scramble: String by mutableStateOf("")
    var isRefreshingPuzzle by mutableStateOf(false)


    fun changeType(type: Puzzles) {
        currentType = type
        config.edit().putString("currentType", currentType.name).apply()
        generateScrambleImage()
    }

    fun generateScrambleImage() {
        scramble = ""
        puzzlePath = ""
        isRefreshingPuzzle = true
        observePuzzle = false
        scrambleScope.launch {
            val scrambleSteps = currentType.puzzle.generateScramble()
            val scrambleSvg = currentType.puzzle.drawScramble(scrambleSteps, hashMapOf())
            val scrambleFile = File(app.getExternalFilesDir(null), "puzzle.svg")
            scrambleFile.writeText(scrambleSvg.toString())
            delay(500)
            viewModelScope.launch {
                scramble = scrambleSteps
                puzzleSvg = scrambleSvg
                puzzlePath = scrambleFile.absolutePath
                isRefreshingPuzzle = false
            }
        }
    }

    fun init() {
        generateScrambleImage()
    }

}
