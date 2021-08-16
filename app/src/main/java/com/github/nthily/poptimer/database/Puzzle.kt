package com.github.nthily.poptimer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.nthily.poptimer.utils.Puzzles

@Entity
data class Puzzle(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "score") val score: Long?,
    @ColumnInfo(name = "time") val time: Long?,
    @ColumnInfo(name = "type") val type: Puzzles
)