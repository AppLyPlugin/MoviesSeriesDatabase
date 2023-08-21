package com.applyplugin.moviesseriesdatabase.repository.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.applyplugin.moviesseriesdatabase.model.MoviesSeriesResponse
import com.applyplugin.moviesseriesdatabase.util.Constants.Companion.MOVIES_TABLE

@Entity(tableName = MOVIES_TABLE)
class MoviesEntity(
    var movies: MoviesSeriesResponse
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}