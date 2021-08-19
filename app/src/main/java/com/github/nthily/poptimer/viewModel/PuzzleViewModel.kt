package com.github.nthily.poptimer.viewModel

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.nthily.poptimer.repository.DataRepository
import com.github.nthily.poptimer.utils.Puzzles
import com.github.nthily.poptimer.utils.Utils
import com.tencent.mmkv.MMKV
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.compat.SharedViewModelCompat
import org.koin.androidx.compose.get
import org.koin.java.KoinJavaComponent.inject
import org.worldcubeassociation.tnoodle.svglite.Svg

/*
class PuzzleViewModel (
    application: Application,
    private val dataRepository: DataRepository
) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext
    private val config = MMKV.defaultMMKV()
    private var scrambleScope = CoroutineScope(Dispatchers.IO)
    private var puzzleSvg: Svg? by mutableStateOf(null)
    var bestScore by mutableStateOf(Long.MAX_VALUE)
    var currentType by mutableStateOf(Puzzles.valueOf(config.getString("currentType", "THREE") ?: "THREE"))
    var puzzlePath by mutableStateOf("")
    var scramble: String by mutableStateOf("")

    fun changeType(type: Puzzles) {
        currentType = type
        config.encode("currentType", currentType.name)
        generateScrambleImage()
    }

    fun generateScrambleImage() {
        scramble = ""
        puzzlePath = ""
        dataRepository.isRefreshingPuzzle.value = true
        dataRepository.isObservingPuzzle.value = false
        scrambleScope.launch {

            val scrambleSteps = currentType.puzzle.generateScramble()
            val scrambleSvg = currentType.puzzle.drawScramble(scrambleSteps, hashMapOf())
            if(!File(context.filesDir.absolutePath + "/puzzle").exists()) File(context.filesDir.absolutePath + "/puzzle").mkdirs()
            File(context.filesDir.absolutePath + "/puzzle", "puzzle.svg").writeText(scrambleSvg.toString())
            delay(500)
            viewModelScope.launch {
                scramble = scrambleSteps
                puzzleSvg = scrambleSvg
                puzzlePath = context.filesDir.absolutePath + "/puzzle/puzzle.svg"
                dataRepository.isRefreshingPuzzle.value = false
            }
        }

    }

    fun init() {
        generateScrambleImage()
    }

}


 */