package com.github.nthily.poptimer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Database(
    entities = [Puzzle::class], version = 1
)
abstract class PuzzleDatabase : RoomDatabase() {
    abstract fun getPuzzleDao() : PuzzleDao
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun PuzzleDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        PuzzleDatabase::class.java,
        "puzzle_database"
    ).build()

    @Singleton
    @Provides
    fun getDao(db: PuzzleDatabase) = db.getPuzzleDao()
}

/*
@Database(
    entities = [Puzzle::class], version = 1
)
abstract class PuzzleDatabase : RoomDatabase() {
    abstract fun puzzleDao(): PuzzleDao

    private class PuzzleDataBaseCallBack(
        private val scope: CoroutineScope
    ): RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val puzzleDao = database.puzzleDao()
                    puzzleDao.insert(Puzzle(0, 9L, 5L, Puzzles.TWO))
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
                )
                    .addCallback(PuzzleDataBaseCallBack(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}

 */