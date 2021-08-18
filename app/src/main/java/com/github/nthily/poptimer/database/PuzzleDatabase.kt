package com.github.nthily.poptimer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Database(entities = [Puzzle::class], version = 1, exportSchema = false)
abstract class PuzzleDatabase : RoomDatabase() {
    abstract fun getPuzzleDao() : PuzzleDao
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Volatile
    private var INSTANCE: PuzzleDatabase? = null

    private class PuzzleDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { _ ->
                scope.launch {
                    // you can pre-populate some data here
                    // database.getPuzzleDao().insert(Puzzle(0, 90000L, 2L, Puzzles.TWO))
                }
            }
        }
    }

    @Singleton
    @Provides
    fun puzzleDataBase(
        @ApplicationContext app: Context
    ) : PuzzleDatabase {
        return INSTANCE ?: synchronized(this) {
            val scope = CoroutineScope(Dispatchers.IO)
            val instance = Room.databaseBuilder(
                app,
                PuzzleDatabase::class.java,
                "puzzle_database"
            )   .addCallback(PuzzleDatabaseCallback(scope))
                .build()
                .also {
                    INSTANCE = it
                }
            instance
        }
    }

    @Singleton
    @Provides
    fun getDao(db: PuzzleDatabase) = db.getPuzzleDao()

}
