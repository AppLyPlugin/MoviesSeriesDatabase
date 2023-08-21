package com.applyplugin.moviesseriesdatabase.repository.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.applyplugin.moviesseriesdatabase.model.MoviesSeriesResultDetails
import com.applyplugin.moviesseriesdatabase.util.Constants.Companion.WATCHLIST_TABLE

@Entity(tableName = WATCHLIST_TABLE)
class WatchlistEntity(
    var data: MoviesSeriesResultDetails
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int? = data.id
}