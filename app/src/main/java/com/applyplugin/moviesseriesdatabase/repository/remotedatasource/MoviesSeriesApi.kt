package com.applyplugin.moviesseriesdatabase.repository.remotedatasource

import com.applyplugin.moviesseriesdatabase.model.MoviesSeriesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface MoviesSeriesApi {

    @GET("/3/discover/movie?")
    suspend fun getMovies(
        @QueryMap queries: HashMap<String, String>
    ): Response<MoviesSeriesResponse>

    @GET("/3/search/movie?")
    suspend fun searchMovies(
        @QueryMap queries: HashMap<String, String>
    ): Response<MoviesSeriesResponse>


    @GET("/3/discover/tv?")
    suspend fun getSeries(
        @QueryMap queries: HashMap<String, String>
    ): Response<MoviesSeriesResponse>

    @GET("/3/search/tv?")
    suspend fun searchSeries(
        @QueryMap queries: HashMap<String, String>
    ): Response<MoviesSeriesResponse>

}