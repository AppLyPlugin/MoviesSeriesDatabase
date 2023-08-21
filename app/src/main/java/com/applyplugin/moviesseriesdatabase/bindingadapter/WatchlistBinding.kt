package com.applyplugin.moviesseriesdatabase.bindingadapter

import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.applyplugin.moviesseriesdatabase.adapter.WatchlistAdapter
import com.applyplugin.moviesseriesdatabase.repository.database.entity.WatchlistEntity

class WatchlistBinding {

    companion object {

        @BindingAdapter("setVisibility", "setData", requireAll = false)
        @JvmStatic
        fun erroView(
            view: View,
            database: List<WatchlistEntity>?,
            mAdapter: WatchlistAdapter?
        ) {
            when (view) {
                is RecyclerView -> {
                    var dataBaseCheck = database.isNullOrEmpty()
                    view.isInvisible = dataBaseCheck

                    if (!dataBaseCheck) {
                        database?.let { mAdapter?.setData(it) }
                    }
                }

                else -> view.isVisible = database.isNullOrEmpty()
            }

        }
    }
}
