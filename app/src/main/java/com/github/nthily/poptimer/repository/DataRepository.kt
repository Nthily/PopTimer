package com.github.nthily.poptimer.repository

import androidx.lifecycle.MutableLiveData
import com.github.nthily.poptimer.database.PuzzleDao

class DataRepository(
    val puzzleDao: PuzzleDao
) {
    val all = puzzleDao.getAll()
    var isRefreshingPuzzle = MutableLiveData(false)
    var isObservingPuzzle = MutableLiveData(false)
    var bottomNavigationItem = MutableLiveData(1)
}