package com.applyplugin.moviesseriesdatabase.bindingadapter

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.applyplugin.moviesseriesdatabase.model.MoviesSeriesResponse
import com.applyplugin.moviesseriesdatabase.repository.database.entity.MoviesEntity
import com.applyplugin.moviesseriesdatabase.util.NetworkResult

class MoviesFragmentBinding {

    companion object {

        @BindingAdapter("readApiResponse", "readDatabase", requireAll = true)
        @JvmStatic
        fun errorImageViewVisibility(
            view: View,
            apiResponse: NetworkResult<MoviesSeriesResponse>?,
            database: List<MoviesEntity>?
        ) {
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()) {
                view.visibility = View.VISIBLE
                when(view){
                    is TextView -> view.text = apiResponse.message.toString()
                }
            }else{
                view.visibility = View.INVISIBLE
            }
        }
    }
}