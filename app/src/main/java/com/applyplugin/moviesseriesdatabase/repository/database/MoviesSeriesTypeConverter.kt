package com.applyplugin.movieseriesdatabase.repository.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.applyplugin.moviesseriesdatabase.model.MoviesSeriesResponse
import com.applyplugin.moviesseriesdatabase.model.MoviesSeriesResultDetails
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class MoviesSeriesTypeConverter {

    private var gson = Gson()

    @TypeConverter
    fun moviesResponse(moviesResponse: MoviesSeriesResponse): String{
        return gson.toJson(moviesResponse)
    }

    @TypeConverter
    fun stringToMoviesResponse(data: String): MoviesSeriesResponse{
        var listType = object : TypeToken<MoviesSeriesResponse>(){}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun movieItemToString(movie: MoviesSeriesResultDetails): String{
        return gson.toJson(movie)
    }

    @TypeConverter
    fun stringToMovieItem(data: String): MoviesSeriesResultDetails{
        var type = object : TypeToken<MoviesSeriesResultDetails>(){}.type
        return gson.fromJson(data, type)
    }

}