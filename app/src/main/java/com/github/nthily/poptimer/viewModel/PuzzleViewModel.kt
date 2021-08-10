package com.github.nthily.poptimer.viewModel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
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
class PuzzleViewModel @Inject constructor()
    : ViewModel() {


    var currentPuzzleType by mutableStateOf(3)
    var scramble: String by mutableStateOf("")
    private var puzzleSvg: Svg? by mutableStateOf(null)
    var puzzleFileLength by mutableStateOf(0L)
    var puzzlePath by mutableStateOf("")

    fun getScramble(context: Context, viewModel: AppViewModel) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                puzzleFileLength = 0L
                viewModel.isRefreshingPuzzle = true
                viewModel.observePuzzle = false
            }
            scramble = ""
            delay(500)
            scramble = NbyNCubePuzzle(currentPuzzleType).generateScramble()
            puzzleSvg = NbyNCubePuzzle(currentPuzzleType).drawScramble(scramble, hashMapOf())
            val file = File(context.getExternalFilesDir(null), "puzzle.svg")
            file.writeText(puzzleSvg.toString())
            withContext(Dispatchers.Main){
                puzzleFileLength = file.length()
                puzzlePath = file.absolutePath
                viewModel.isRefreshingPuzzle = false
            }
        }
    }
}