package com.applyplugin.moviesseriesdatabase.model


import com.google.gson.annotations.SerializedName

data class MoviesSeriesResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val moviesseriesResult: List<MoviesSeriesResultDetails>?,
    @SerializedName("total_pages")
    val totalPage: Int?,
    @SerializedName("total_results")
    val totalResult: Int?
)