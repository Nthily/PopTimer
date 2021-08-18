package com.github.nthily.poptimer.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PuzzleDao {

    @Query("SELECT * FROM puzzle")
    fun getAll(): Flow<List<Puzzle>>

    @Insert
    fun insert(puzzle: Puzzle)

}
