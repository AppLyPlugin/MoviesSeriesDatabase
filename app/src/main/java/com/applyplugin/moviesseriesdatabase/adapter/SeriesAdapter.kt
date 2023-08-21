package com.applyplugin.moviesseriesdatabase.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.applyplugin.moviesseriesdatabase.databinding.SeriesRowLayoutBinding
import com.applyplugin.moviesseriesdatabase.model.MoviesSeriesResultDetails
import com.applyplugin.moviesseriesdatabase.util.MoviesSeriesDiffUtil

class SeriesAdapter : RecyclerView.Adapter<SeriesAdapter.MyViewHolder>() {

    private var series = emptyList<MoviesSeriesResultDetails>()

    class MyViewHolder(private val binding: SeriesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(series: MoviesSeriesResultDetails) {
            binding.series = series
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SeriesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentResult = series[position]
        holder.bind(currentResult)
    }

    override fun getItemCount(): Int {
        return series.size
    }

    fun setData(newData: List<MoviesSeriesResultDetails>) {
        val seriesDiffUtil = MoviesSeriesDiffUtil(series, newData)
        val diffUtilResult = DiffUtil.calculateDiff(seriesDiffUtil)
        series = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}