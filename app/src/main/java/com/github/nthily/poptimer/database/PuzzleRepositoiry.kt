package com.github.nthily.poptimer.database

import javax.inject.Inject

class PuzzleRepository @Inject constructor (
    val puzzleDao: PuzzleDao
) {
    val all = puzzleDao.getAll()
}
