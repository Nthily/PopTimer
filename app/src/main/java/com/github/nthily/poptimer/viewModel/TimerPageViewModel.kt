package com.github.nthily.poptimer.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.nthily.poptimer.database.Puzzle
import com.github.nthily.poptimer.repository.DataRepository
import com.github.nthily.poptimer.utils.Puzzles
import com.tencent.mmkv.MMKV
import java.io.File
import java.time.ZonedDateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.worldcubeassociation.tnoodle.svglite.Svg

class TimerPageViewModel (
    application: Application,
    private val dataRepository: DataRepository
) : AndroidViewModel(application) {

    /*
     about timer value
    */

    var startTime = 0L
    var time by mutableStateOf(0L)
    var isTiming by mutableStateOf(false)
    var ready by mutableStateOf(false)
    var lastResult by mutableStateOf<Long?>(null)
    private var tempResult: Long? = null

    fun readyStage() {
        tempResult = time
        time = 0L
        ready = true
    }

    fun start() {
        startTime = System.currentTimeMillis()
        isTiming = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun stop() {
        val timestamp: Long = ZonedDateTime.now().toEpochSecond()
        viewModelScope.launch {
            isTiming = false
            lastResult = if(tempResult != 0L) tempResult else null
            ready = false
            if(time < bestScore) {
                bestScore = time
            }
            withContext(Dispatchers.IO) {
                dataRepository.puzzleDao.insert(
                    Puzzle(
                        id = 0,
                        solveTime = time,
                        timestamp = timestamp,
                        type = currentType
                    )
                )
            }
        }
    }

    /*
      about puzzle
     */

    @SuppressLint("StaticFieldLeak")
    private val context = application.applicationContext
    private val config = MMKV.defaultMMKV()
    private var scrambleScope = CoroutineScope(Dispatchers.IO)
    private var puzzleSvg: Svg? by mutableStateOf(null)
    var bestScore by mutableStateOf(Long.MAX_VALUE)
    var currentType by mutableStateOf(Puzzles.valueOf(config.getString("currentType", "THREE") ?: "THREE"))
    var puzzlePath by mutableStateOf("")
    var scramble: String by mutableStateOf("")
    var selectPuzzle by mutableStateOf(false)

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
