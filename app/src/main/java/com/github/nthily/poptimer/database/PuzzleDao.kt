package com.github.nthily.poptimer.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import javax.inject.Inject

@Dao
interface PuzzleDao {

    @Query("SELECT * FROM puzzle")
    fun getAll(): LiveData<List<Puzzle>>

    @Insert
    fun insert(puzzle: Puzzle)

}

/*
class PuzzleRepository @Inject constructor(
    val puzzleDao: PuzzleDao
)
 */