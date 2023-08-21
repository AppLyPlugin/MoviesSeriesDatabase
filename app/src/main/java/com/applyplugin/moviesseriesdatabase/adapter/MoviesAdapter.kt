package com.applyplugin.moviesseriesdatabase.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.applyplugin.moviesseriesdatabase.databinding.MoviesRowLayoutBinding
import com.applyplugin.moviesseriesdatabase.model.MoviesSeriesResultDetails
import com.applyplugin.moviesseriesdatabase.util.MoviesSeriesDiffUtil

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MyViewHolder>() {

    private var movies = emptyList<MoviesSeriesResultDetails>()

    class MyViewHolder(private val binding: MoviesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movies: MoviesSeriesResultDetails) {
            binding.movies = movies
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MoviesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentResult = movies[position]
        holder.bind(currentResult)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun setData(newData: List<MoviesSeriesResultDetails>) {
        val moviesDiffUtil = MoviesSeriesDiffUtil(movies, newData)
        val diffUtilResult = DiffUtil.calculateDiff(moviesDiffUtil)
        movies = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

}