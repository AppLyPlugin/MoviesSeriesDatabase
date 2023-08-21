package com.applyplugin.moviesseriesdatabase.repository

import com.applyplugin.movieseriesdatabase.repository.database.MoviesSeriesDao
import com.applyplugin.moviesseriesdatabase.repository.database.entity.MoviesEntity
import com.applyplugin.moviesseriesdatabase.repository.database.entity.SeriesEntity
import com.applyplugin.moviesseriesdatabase.repository.database.entity.WatchlistEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class LocalDataSource @Inject constructor(
    private val moviesSeriesDao: MoviesSeriesDao
) {

    fun readMovies(): Flow<List<MoviesEntity>> {
        return moviesSeriesDao.readMovies()
    }

    fun readSeries(): Flow<List<SeriesEntity>>{
        return moviesSeriesDao.readSeries()
    }

    suspend fun insertMovies(moviesEntity: MoviesEntity){
        moviesSeriesDao.insertMovies(moviesEntity)
    }

    suspend fun insertSeries(seriesEntity: SeriesEntity){
        moviesSeriesDao.insertSeries(seriesEntity)
    }

    fun readWatchlistDB(): Flow<List<WatchlistEntity>>{
        return moviesSeriesDao.readWatchlist()
    }

    suspend fun insertWatchlist(watchlistEntity: WatchlistEntity){
        moviesSeriesDao.insertWatchlist(watchlistEntity)
    }

    suspend fun deleteWatchlist(watchlistEntity: WatchlistEntity){
        moviesSeriesDao.deleteWatchlist(watchlistEntity)
    }

    suspend fun deleteAllWatchlist(){
        moviesSeriesDao.deleteAllWatchlist()
    }

}