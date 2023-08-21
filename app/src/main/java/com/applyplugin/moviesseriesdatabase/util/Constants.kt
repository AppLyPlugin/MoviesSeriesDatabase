package com.applyplugin.moviesseriesdatabase.util

class Constants {

    companion object {
        val BASE_URL: String = "https://api.themoviedb.org/"

        //Database
        const val MOVIES_SERIES_DATABASE = "movies_series_database"
        const val SERIES_TABLE = "series_table"
        const val MOVIES_TABLE = "movies_table"
        const val WATCHLIST_TABLE = "watchlist_table"

        //Query Keys
        const val QUERY_YEAR = "primary_release_year"
        const val QUERY_FIRST_AIR_YEAR = "first_air_date_year"
        const val QUERY_SORT = "sort_by"
        const val QUERY_SEARCH = "query"
        const val QUERY_PAGE = "page"

        //Bottom Sheet and Pref
        const val PREF_NAME = "moviesseries_preferences"
        const val DEFAULT_YEAR = "2023"
        const val DEFAULT_SORT = "popularity.desc"
        const val YEAR = "primary_release_year"
        const val YEAR_ID = "year_id"
        const val SORT = "sort"
        const val SORT_ID = "sort_id"
        const val BACK_ONLINE = "backOnline"

        val Genre = mutableMapOf(
            28 to "Action",
            12 to "Adventure",
            16 to "Animation",
            35 to "Comedy",
            80 to "Crime",
            99 to "Documentary",
            18 to "Drama",
            10751 to "Family",
            14 to "Fantasy",
            36 to "History",
            27 to "Horror",
            10402 to "Music",
            9648 to "Mystery",
            10749 to "Romance",
            878 to "ScienceFiction",
            10770 to "TVMovie",
            53 to "Thriller",
            10752 to "War",
            37 to "Western"
        )

    }

}