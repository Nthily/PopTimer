package com.github.nthily.poptimer.viewModel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.nthily.poptimer.R
import com.github.nthily.poptimer.utils.Puzzles
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


    var currentPuzzleType by mutableStateOf(R.string.cube_333)
    var puzzleIcon by mutableStateOf(R.drawable.ic_3x3)

    var scramble: String by mutableStateOf("")
    private var puzzleSvg: Svg? by mutableStateOf(null)
    var puzzleFileLength by mutableStateOf(0L)
    var puzzlePath by mutableStateOf("")



    fun generateScramble(context: Context, viewModel: AppViewModel) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                puzzleFileLength = 0L
                viewModel.isRefreshingPuzzle = true
                viewModel.observePuzzle = false
            }
            scramble = ""
            delay(500)
            getPuzzleType()
            val file = File(context.getExternalFilesDir(null), "puzzle.svg")
            file.writeText(puzzleSvg.toString())
            withContext(Dispatchers.Main){
                puzzleFileLength = file.length()
                puzzlePath = file.absolutePath
                viewModel.isRefreshingPuzzle = false
            }
        }
    }

    private fun getPuzzleType(){
        when(currentPuzzleType) {
            R.string.cube_222 -> {
                scramble = Puzzles.TWO.puzzle.generateScramble()
                puzzleIcon = R.drawable.ic_2x2
                puzzleSvg = Puzzles.TWO.puzzle.drawScramble(scramble, hashMapOf())
            }
            R.string.cube_333 -> {
                scramble = Puzzles.THREE.puzzle.generateScramble()
                puzzleIcon = R.drawable.ic_3x3
                puzzleSvg = Puzzles.THREE.puzzle.drawScramble(scramble, hashMapOf())
            }
            R.string.cube_444 -> {
                scramble = Puzzles.FOUR.puzzle.generateScramble()
                puzzleIcon = R.drawable.ic_4x4
                puzzleSvg = Puzzles.FOUR.puzzle.drawScramble(scramble, hashMapOf())
            }
            R.string.cube_555 -> {
                scramble = Puzzles.FIVE.puzzle.generateScramble()
                puzzleIcon = R.drawable.ic_5x5
                puzzleSvg = Puzzles.FIVE.puzzle.drawScramble(scramble, hashMapOf())
            }
            R.string.cube_666 -> {
                scramble = Puzzles.SIX.puzzle.generateScramble()
                puzzleIcon = R.drawable.ic_6x6
                puzzleSvg = Puzzles.SIX.puzzle.drawScramble(scramble, hashMapOf())
            }
            R.string.cube_777 -> {
                scramble = Puzzles.SEVEN.puzzle.generateScramble()
                puzzleIcon = R.drawable.ic_7x7
                puzzleSvg = Puzzles.SEVEN.puzzle.drawScramble(scramble, hashMapOf())
            }
            R.string.cube_pyra -> {
                scramble = Puzzles.PYRA.puzzle.generateScramble()
                puzzleIcon = R.drawable.ic_pyra
                puzzleSvg = Puzzles.PYRA.puzzle.drawScramble(scramble, hashMapOf())
            }
            R.string.cube_sq1 -> {
                scramble = Puzzles.SQ1.puzzle.generateScramble()
                puzzleIcon = R.drawable.ic_sq1
                puzzleSvg = Puzzles.SQ1.puzzle.drawScramble(scramble, hashMapOf())
            }
            R.string.cube_mega -> {
                scramble = Puzzles.MEGA.puzzle.generateScramble()
                puzzleIcon = R.drawable.ic_mega
                puzzleSvg = Puzzles.MEGA.puzzle.drawScramble(scramble, hashMapOf())
            }
            R.string.cube_clock -> {
                scramble = Puzzles.CLOCK.puzzle.generateScramble()
                puzzleIcon = R.drawable.ic_clock
                puzzleSvg = Puzzles.CLOCK.puzzle.drawScramble(scramble, hashMapOf())
            }
            R.string.cube_skewb -> {
                scramble = Puzzles.SKEWB.puzzle.generateScramble()
                puzzleIcon = R.drawable.ic_skewb
                puzzleSvg = Puzzles.SKEWB.puzzle.drawScramble(scramble, hashMapOf())
            }
        }
    }

}