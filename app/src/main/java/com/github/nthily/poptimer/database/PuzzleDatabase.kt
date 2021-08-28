package com.github.nthily.poptimer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Puzzle::class], version = 1, exportSchema = false)
abstract class PuzzleDatabase : RoomDatabase() {
    abstract fun getPuzzleDao() : PuzzleDao

    private class PuzzleDataBaseCallBack(
        private val scope: CoroutineScope
    ): RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { /*database -> */
                scope.launch {
                    //val puzzleDao = database.getPuzzleDao()
                    //puzzleDao.insert(Puzzle(0, 9L, 5L, Puzzles.TWO))
                }
            }
        }
    }

    companion object {

        @Volatile
        private var INSTANCE: PuzzleDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): PuzzleDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PuzzleDatabase::class.java,
                    "puzzle_database"
                ) //.addCallback(PuzzleDataBaseCallBack(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
