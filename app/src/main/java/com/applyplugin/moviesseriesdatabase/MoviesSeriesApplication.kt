package com.applyplugin.moviesseriesdatabase

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MoviesSeriesApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}