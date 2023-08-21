package com.applyplugin.moviesseriesdatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.navigation.navArgs
import coil.load
import coil.size.Precision
import coil.size.Scale
import com.applyplugin.moviesseriesdatabase.databinding.MoviesSeriesActivityDetailsBinding
import com.applyplugin.moviesseriesdatabase.repository.database.entity.WatchlistEntity
import com.applyplugin.moviesseriesdatabase.util.Constants
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.time.delay
import java.lang.StringBuilder

@AndroidEntryPoint
class MoviesSeriesDetailsActivity : AppCompatActivity() {
    private lateinit var binding: MoviesSeriesActivityDetailsBinding
    private val mainViewModel: MainViewModel by viewModels()

    private val arg by navArgs<MoviesSeriesDetailsActivityArgs>()

    private var watchlistSaved = false
    private lateinit var menuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MoviesSeriesActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setDetailsActivity()
    }

    private fun setDetailsActivity() {

        this.title = arg.moviesSeriesDetails?.title
        binding.detailsBanner.load(
            "http://image.tmdb.org/t/p/w500${arg.moviesSeriesDetails?.backdropPath}"
        ) {
            crossfade(true)
            placeholder(R.drawable.ic_error_loading_image)
            error(R.drawable.ic_error_loading_image)
            precision(Precision.EXACT)
            scale(Scale.FILL)
        }

        binding.detailsTitle.text = arg.moviesSeriesDetails!!.title
        if (arg.moviesSeriesDetails?.originalLanguage != "en") {
            binding.detailsOriginaltitle.visibility = View.VISIBLE
            binding.detailsOriginaltitle.text = arg.moviesSeriesDetails?.originalTitle
        } else {
            binding.detailsOriginaltitle.visibility = View.GONE
        }
        binding.detailsGenre.text = setGenres(arg.moviesSeriesDetails!!.genreIds!!)
        binding.detailsSummary.text = arg.moviesSeriesDetails!!.overview
        binding.detailsReleasedate.text = arg.moviesSeriesDetails!!.releaseDate?.replace("-", "/")
        binding.detailsOrigLang.text = arg.moviesSeriesDetails!!.originalLanguage?.uppercase()
        binding.detailsRating.text = arg.moviesSeriesDetails!!.voteAverage.toString()
        setRatingTextColor(binding.detailsRating, arg.moviesSeriesDetails!!.voteAverage)
    }

    private fun setRatingTextColor(textView: TextView, voteAverage: Double?) {
        var textColor = when {
            voteAverage!! < 4 -> R.color.negative
            voteAverage!! > 6 -> R.color.positive
            else -> R.color.no_change
        }

        textView.setTextColor(ContextCompat.getColor(textView.context, textColor))
    }

    fun setGenres(genre: List<Int>): String {
        var genres = ""
        val sb = StringBuilder()

        for (genreId in genre) {
            sb.append(Constants.Genre[genreId] ?: "")
            sb.append(" ")
        }

        return sb.toString()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        menuItem = menu!!.findItem(R.id.save_to_watchlist_menu)
        checkWatchlistMovieData(menuItem)
        return true
    }

    private fun checkWatchlistMovieData(menuItem: MenuItem) {
        mainViewModel.readWatchlist.observe(this) { watchlistEntity ->
            try {
                for (watchlistItem in watchlistEntity) {
                    if (watchlistItem.data.id == arg.moviesSeriesDetails!!.id) {
                        watchlistSaved = true;
                        changeMenuItemColor(menuItem, R.color.yellow)
                        break;
                    } else {
                        watchlistSaved = false;
                    }
                }
            } catch (e: Exception) {
                Log.d("MoviesSeriesDetailsActivity.kt", e.message.toString())
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.save_to_watchlist_menu && !watchlistSaved) {
            saveToWatchlist(item)
        } else if (item.itemId == R.id.save_to_watchlist_menu && watchlistSaved) {
            deleteFromWatchlist(item)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun saveToWatchlist(item: MenuItem) {
        val watchlistEntity =
            WatchlistEntity(
                arg.moviesSeriesDetails!!
            )
        mainViewModel.insertWatchlistData(watchlistEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Item Saved")
        watchlistSaved = true
    }

    private fun deleteFromWatchlist(item: MenuItem) {
        val watchlistEntity =
            WatchlistEntity(
                arg.moviesSeriesDetails!!
            )
        mainViewModel.deleteWatchlist(watchlistEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Deleted from Watchlist")
        watchlistSaved = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.detailsLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("OK") {}
            .show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon?.setTint(ContextCompat.getColor(this, color))
    }

    override fun onDestroy() {
        super.onDestroy()
        changeMenuItemColor(menuItem, R.color.white)
    }

}