package com.applyplugin.movieseriesdatabase.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.applyplugin.moviesseriesdatabase.di.DatabaseModule.provideDatabase
import com.applyplugin.moviesseriesdatabase.repository.database.entity.MoviesEntity
import com.applyplugin.moviesseriesdatabase.repository.database.entity.SeriesEntity
import com.applyplugin.moviesseriesdatabase.repository.database.entity.WatchlistEntity

@Database(
    entities = [MoviesEntity::class, SeriesEntity::class, WatchlistEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(MoviesSeriesTypeConverter::class)
abstract class MoviesSeriesDatabase: RoomDatabase() {

    abstract fun moviesSeriesDao(): MoviesSeriesDao

    companion object{
        @Volatile
        private var instance: MoviesSeriesDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: provideDatabase(context).also{ instance = it}
        }

    }

}