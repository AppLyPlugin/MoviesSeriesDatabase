package com.applyplugin.movieseriesdatabase.repository.database

import androidx.room.*
import com.applyplugin.moviesseriesdatabase.repository.database.entity.MoviesEntity
import com.applyplugin.moviesseriesdatabase.repository.database.entity.SeriesEntity
import com.applyplugin.moviesseriesdatabase.repository.database.entity.WatchlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesSeriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(moviesEntity: MoviesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeries(seriesEntity: SeriesEntity)

    @Query("SELECT * FROM MOVIES_TABLE ORDER BY id ASC")
    fun readMovies(): Flow<List<MoviesEntity>>

    @Query("SELECT * FROM SERIES_TABLE ORDER BY id ASC")
    fun readSeries(): Flow<List<SeriesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchlist(watchlistEntity: WatchlistEntity)

    @Query("SELECT * FROM WATCHLIST_TABLE ORDER BY id ASC")
    fun readWatchlist(): Flow<List<WatchlistEntity>>

    @Delete
    suspend fun deleteWatchlist(watchlistEntity: WatchlistEntity)

    @Query("DELETE FROM WATCHLIST_TABLE")
    suspend fun deleteAllWatchlist()

}