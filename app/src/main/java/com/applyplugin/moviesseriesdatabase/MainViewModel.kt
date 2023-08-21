package com.applyplugin.moviesseriesdatabase

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.applyplugin.moviesseriesdatabase.model.MoviesSeriesResponse
import com.applyplugin.moviesseriesdatabase.repository.Repository
import com.applyplugin.moviesseriesdatabase.repository.database.entity.MoviesEntity
import com.applyplugin.moviesseriesdatabase.repository.database.entity.SeriesEntity
import com.applyplugin.moviesseriesdatabase.repository.database.entity.WatchlistEntity
import com.applyplugin.moviesseriesdatabase.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /*********ROOM DATABASE*********/
    private fun insertMovieData(moviesEntity: MoviesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.localSource.insertMovies(moviesEntity)
        }

    private fun insertSeriesData(seriesEntity: SeriesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.localSource.insertSeries(seriesEntity)
        }

    fun insertWatchlistData(watchlistEntity: WatchlistEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.localSource.insertWatchlist(watchlistEntity)
        }

    val readMoviesData: LiveData<List<MoviesEntity>> =
        repository.localSource.readMovies().asLiveData()

    val readSeriesData: LiveData<List<SeriesEntity>> =
        repository.localSource.readSeries().asLiveData()

    val readWatchlist: LiveData<List<WatchlistEntity>> =
        repository.localSource.readWatchlistDB().asLiveData()

    fun deleteWatchlist(watchlistEntity: WatchlistEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.localSource.deleteWatchlist(watchlistEntity)
        }

    fun deleteAllWatchlist() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.localSource.deleteAllWatchlist()
        }

    /************ RETROFIT ************/

    var moviesseriesResponse: MutableLiveData<NetworkResult<MoviesSeriesResponse>> =
        MutableLiveData()

    fun getMoviesSeries(query: HashMap<String, String>) = viewModelScope.launch {
        getMoviesSafeCall(query)
    }

    fun getSeries(query: HashMap<String, String>) = viewModelScope.launch {
        getSeriesSafeCall(query)
    }

    fun searchMovies(query: HashMap<String, String>) = viewModelScope.launch {
        searchMoviesSafeCall(query)
    }

    fun searchSeries(query: HashMap<String, String>) = viewModelScope.launch {
        searchSeriesSafeCall(query)
    }

    private suspend fun getMoviesSafeCall(query: HashMap<String, String>) {
        moviesseriesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remoteSource.getMovies(query)
                moviesseriesResponse.value = this.handleMoviesSeriesResponse(response)
                val movies = moviesseriesResponse.value!!.data
                if (movies != null) {
                    offlineCacheMovieData(movies)
                }
            } catch (e: Exception) {
                moviesseriesResponse.value = NetworkResult.Error("Data Not Found")
                e.stackTrace
            }
        } else {
            moviesseriesResponse.value = NetworkResult.Error("No Internet Connection")
        }

    }

    private suspend fun getSeriesSafeCall(query: HashMap<String, String>) {
        moviesseriesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remoteSource.getSeries(query)
                moviesseriesResponse.value = this.handleMoviesSeriesResponse(response)
                val series = moviesseriesResponse.value!!.data
                if (series != null) {
                    offlineCacheSeriesData(series)
                }
            } catch (e: Exception) {
                moviesseriesResponse.value = NetworkResult.Error("Data Not Found")
                e.stackTrace
            }
        } else {
            moviesseriesResponse.value = NetworkResult.Error("No Internet Connection")
        }

    }

    private suspend fun searchMoviesSafeCall(searchQuery: HashMap<String, String>) {
        moviesseriesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            Log.d("MainViewModel", "Has Internet Connection!")
            try {
                val response = repository.remoteSource.searchMovies(searchQuery)
                moviesseriesResponse.value = this.handleMoviesSeriesResponse(response)
            } catch (e: Exception) {
                moviesseriesResponse.value = NetworkResult.Error("Error API Limitation")
                e.stackTrace
            }
        } else {
            moviesseriesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private suspend fun searchSeriesSafeCall(searchQuery: HashMap<String, String>) {
        moviesseriesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            Log.d("MainViewModel", "Has Internet Connection!")
            try {
                val response = repository.remoteSource.searchSeries(searchQuery)
                moviesseriesResponse.value = this.handleMoviesSeriesResponse(response)
            } catch (e: Exception) {
                moviesseriesResponse.value = NetworkResult.Error("Error API Limitation")
                e.stackTrace
            }
        } else {
            moviesseriesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private fun offlineCacheMovieData(movies: MoviesSeriesResponse) {
        val moviesEntity = MoviesEntity(movies)
        insertMovieData(moviesEntity)
    }

    private fun offlineCacheSeriesData(series: MoviesSeriesResponse) {
        val seriesEntity = SeriesEntity(series)
        insertSeriesData(seriesEntity)
    }

    private fun handleMoviesSeriesResponse(response: Response<MoviesSeriesResponse>): NetworkResult<MoviesSeriesResponse>? {

        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Connection Timeout")
            }

            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited")
            }

            response.body()!!.toString().isNullOrEmpty() -> {
                return NetworkResult.Error("Data Not Found")
            }

            response.code() == 200 ||
                    response.isSuccessful -> {
                val movies = response.body()
                return NetworkResult.Success(movies!!)
            }

            else -> {
                return NetworkResult.Error(response.message())
            }
        }

    }

    private fun hasInternetConnection(): Boolean {

        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

    }

}