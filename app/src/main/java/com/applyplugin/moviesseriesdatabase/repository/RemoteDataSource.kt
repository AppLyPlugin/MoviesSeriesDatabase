package com.applyplugin.moviesseriesdatabase.repository

import com.applyplugin.moviesseriesdatabase.model.MoviesSeriesResponse
import com.applyplugin.moviesseriesdatabase.repository.remotedatasource.MoviesSeriesApi
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val moviesseriesApiInterface: MoviesSeriesApi) {

    suspend fun getMovies(query: HashMap<String, String>): Response<MoviesSeriesResponse>{
        return moviesseriesApiInterface.getMovies(query)
    }

    suspend fun searchMovies(query: HashMap<String, String>): Response<MoviesSeriesResponse>{
        return moviesseriesApiInterface.searchMovies(query)
    }

    suspend fun getSeries(query: HashMap<String, String>): Response<MoviesSeriesResponse>{
        return moviesseriesApiInterface.getSeries(query)
    }
    suspend fun searchSeries(query: HashMap<String, String>): Response<MoviesSeriesResponse>{
        return moviesseriesApiInterface.searchSeries(query)
    }

}