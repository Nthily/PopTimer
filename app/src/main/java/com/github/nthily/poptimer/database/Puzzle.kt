package com.github.nthily.poptimer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.nthily.poptimer.utils.Puzzles
import java.time.LocalDateTime

@Entity
data class Puzzle(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "scramble") val scramble: String,
    @ColumnInfo(name = "solveTime") val solveTime: Long?,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
    @ColumnInfo(name = "type") val type: Puzzles
)
