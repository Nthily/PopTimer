package com.github.nthily.poptimer.repository

import com.github.nthily.poptimer.database.PuzzleDao

class DataRepository(
    val puzzleDao: PuzzleDao
) {
    val all = puzzleDao.getAll()
}