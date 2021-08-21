package com.github.nthily.poptimer.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import com.github.nthily.poptimer.components.Destinations
import com.github.nthily.poptimer.database.PuzzleDao

class DataRepository(
    val puzzleDao: PuzzleDao
) {
    val all = puzzleDao.getAll()
    var bottomNavigationItem = MutableLiveData(Destinations.TIMERPAGE)
}