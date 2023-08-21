package com.applyplugin.moviesseriesdatabase.adapter

import android.opengl.Visibility
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import coil.size.Precision
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.applyplugin.moviesseriesdatabase.R
import com.applyplugin.moviesseriesdatabase.model.MoviesSeriesResultDetails
import com.applyplugin.moviesseriesdatabase.ui.MoviesFragmentDirections
import com.applyplugin.moviesseriesdatabase.ui.SeriesFragmentDirections
import com.applyplugin.moviesseriesdatabase.util.Constants.Companion.Genre
import java.lang.StringBuilder

class MoviesSeriesRowBinding {

    companion object {

        @BindingAdapter("onMovieItemClickListener")
        @JvmStatic
        fun onMovieItemClickListener(moviesRowLayout: ConstraintLayout, details: MoviesSeriesResultDetails) {
            moviesRowLayout.setOnClickListener {
                try {
                    val action =
                        MoviesFragmentDirections.actionMoviesFragmentToDetailsActivity(details)
                    moviesRowLayout.findNavController().navigate(action)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }

        @BindingAdapter("onSeriesItemClickListener")
        @JvmStatic
        fun onSeriesItemClickListener(seriesRowLayout: ConstraintLayout, details: MoviesSeriesResultDetails) {
            seriesRowLayout.setOnClickListener {
                try {
                    val action =
                        SeriesFragmentDirections.actionSeriesFragmentToDetailsActivity(details)
                    seriesRowLayout.findNavController().navigate(action)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }


        @BindingAdapter("imageUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String?) {
            imageView.load(
                "http://image.tmdb.org/t/p/w500$imageUrl"
            ) {
                crossfade(true)
                placeholder(R.drawable.ic_error_loading_image)
                error(R.drawable.ic_error_loading_image)
                transformations(RoundedCornersTransformation(25f))
                precision(Precision.EXACT)
                size(100, 150)
                scale(Scale.FILL)
            }

        }

        @BindingAdapter("doubleToString")
        @JvmStatic
        fun doubleToString(textView: TextView, value: Double){
            textView.text = value.toString()
        }

        @BindingAdapter(value= ["originalLang", "originalTitle"], requireAll=true)
        @JvmStatic
        fun setOriginalTitle(textView: TextView, originalLang: String, originalTitle: String){
            if(originalLang != "en"){
                textView.visibility = View.VISIBLE
                textView.textSize = 8F
                textView.text = originalTitle
            }else{
                textView.visibility = View.GONE
            }
        }


        @BindingAdapter("setValueFontColor")
        @JvmStatic
        fun setValueFontColor(textView: TextView, rating: Double) {
            var textColor = when {
                rating < 4 -> R.color.negative
                rating > 6 -> R.color.positive
                else -> R.color.no_change
            }

            textView.setTextColor(ContextCompat.getColor(textView.context, textColor))
        }

        @BindingAdapter("setGenres")
        @JvmStatic
        fun setGenres(textView: TextView, genre: List<Int>){
            var genres = ""
            val sb = StringBuilder()

            for(genreId in genre){
                sb.append(Genre[genreId]?:"")
                sb.append(" ")
            }

            textView.text = sb

        }

    }
}