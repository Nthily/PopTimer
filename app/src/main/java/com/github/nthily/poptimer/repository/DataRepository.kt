package com.github.nthily.poptimer.repository

import com.github.nthily.poptimer.database.Puzzle
import com.github.nthily.poptimer.database.PuzzleDao
import kotlinx.coroutines.flow.Flow

class DataRepository(
    val puzzleDao: PuzzleDao
) {
    val all = puzzleDao.getAll()
    val query : (Int) -> Flow<Puzzle> = fun (primaryKey: Int) : Flow<Puzzle> {
        return puzzleDao.searchQuery(primaryKey)
    }
}