package com.github.nthily.poptimer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.nthily.poptimer.utils.Puzzles
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/*
@Database(entities = [Puzzle::class], version = 1, exportSchema = false)
abstract class PuzzleDatabase : RoomDatabase() {
    abstract fun getPuzzleDao() : PuzzleDao
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun PuzzleDatabase(
        @ApplicationContext app: Context,
        scope: CoroutineScope
    ) = Room.databaseBuilder(
        app,
        PuzzleDatabase::class.java,
        "puzzle_database"
    ).build()

    @Singleton
    @Provides
    fun getDao(db: PuzzleDatabase) = db.getPuzzleDao()
}


 */

@Database(
    entities = [Puzzle::class], version = 1, exportSchema = false
)
abstract class PuzzleDatabase : RoomDatabase() {
    abstract fun puzzleDao(): PuzzleDao

    companion object {

        @Volatile
        private var INSTANCE: PuzzleDatabase? = null

        fun getDatabase(
            context: Context
        ): PuzzleDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PuzzleDatabase::class.java,
                    "puzzle_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
