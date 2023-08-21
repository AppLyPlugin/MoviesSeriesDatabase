package com.applyplugin.moviesseriesdatabase.di

import android.content.Context
import androidx.room.Room
import com.applyplugin.movieseriesdatabase.repository.database.MoviesSeriesDatabase
import com.applyplugin.movieseriesdatabase.repository.database.MoviesSeriesTypeConverter
import com.applyplugin.moviesseriesdatabase.util.Constants.Companion.MOVIES_SERIES_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        MoviesSeriesDatabase::class.java,
        MOVIES_SERIES_DATABASE
    ).addTypeConverter(MoviesSeriesTypeConverter())
        .build()


    @Singleton
    @Provides
    fun provideDao(database: MoviesSeriesDatabase) = database.moviesSeriesDao()

}