package com.applyplugin.moviesseriesdatabase.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.applyplugin.moviesseriesdatabase.repository.DataStoreRepository
import com.applyplugin.moviesseriesdatabase.util.Constants.Companion.DEFAULT_SORT
import com.applyplugin.moviesseriesdatabase.util.Constants.Companion.DEFAULT_YEAR
import com.applyplugin.moviesseriesdatabase.util.Constants.Companion.QUERY_FIRST_AIR_YEAR
import com.applyplugin.moviesseriesdatabase.util.Constants.Companion.QUERY_SEARCH
import com.applyplugin.moviesseriesdatabase.util.Constants.Companion.QUERY_SORT
import com.applyplugin.moviesseriesdatabase.util.Constants.Companion.QUERY_YEAR
import com.applyplugin.moviesseriesdatabase.util.Constants.Companion.SORT
import com.applyplugin.moviesseriesdatabase.util.Constants.Companion.YEAR
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesSeriesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    private var year = YEAR
    private var sort = SORT

    var networkStatus = false
    var backOnline = false
    var appStarted = true;

    val readMoviesSeriesFilter = dataStoreRepository.readMoviesSeriesFilter
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    fun saveMoviesSeriesFilter(year: String, yearId: Int, order: String, orderId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMoviesSeriesFilter(year, yearId, order, orderId)
        }

    private fun saveBackOnline(backOnline: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(backOnline)
        }

    fun applyMoviesQuery(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            readMoviesSeriesFilter.collect { value ->
                sort = value.selectedOrder
                year = value.selectedYear
            }

        }

        if (year == YEAR) {
            year = DEFAULT_YEAR
        }else if(year =="all time"){
            year = ""
        }

        if (sort == SORT) {
            sort = DEFAULT_SORT
        }

        queries[QUERY_YEAR] = year
        queries[QUERY_SORT] = sort

        while (queries.values.removeIf { it == "" }) {
        }

        return queries
    }

    fun applyMoviesSearchQuery(search: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            readMoviesSeriesFilter.collect { value ->
                sort = value.selectedOrder
                year = value.selectedYear
            }

        }

        if (year == YEAR) {
            year = DEFAULT_YEAR
        }else if(year =="all time"){
            year = ""
        }

        queries[QUERY_YEAR] = year
        queries[QUERY_SEARCH] = search

        while (queries.values.removeIf { it == "" }) {
        }

        return queries
    }

    fun applySeriesQuery(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            readMoviesSeriesFilter.collect { value ->
                year = value.selectedYear
                sort = value.selectedOrder
            }
        }

        if(year == YEAR){
            year = DEFAULT_YEAR
        }

        if(sort == SORT){
            sort = DEFAULT_SORT
        }

        queries[QUERY_FIRST_AIR_YEAR] = year
        queries[QUERY_SORT] = sort

        while (queries.values.removeIf { it == "" }) {

        }

        return queries
    }

    fun applySeriesSearchQuery(search: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            readMoviesSeriesFilter.collect { value ->
                sort = value.selectedOrder
                year = value.selectedYear
            }

        }

        if (year == YEAR) {
            year = DEFAULT_YEAR
        }else if(year =="all time"){
            year = ""
        }

        queries[QUERY_FIRST_AIR_YEAR] = year
        queries[QUERY_SEARCH] = search

        while (queries.values.removeIf { it == "" }) {
        }

        return queries
    }


    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No Internet Connection", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        } else {
            if (backOnline) {
                Toast.makeText(getApplication(), "We're back online!", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }
}