package com.github.nthily.poptimer.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PuzzleDao {

    @Query("SELECT * FROM puzzle")
    fun getAll(): Flow<List<Puzzle>>

    @Insert
    fun insert(puzzle: Puzzle)

    @Query("SELECT * FROM puzzle WHERE id == :primaryKey")
    fun searchQuery(primaryKey: Int) : Flow<Puzzle>
}
