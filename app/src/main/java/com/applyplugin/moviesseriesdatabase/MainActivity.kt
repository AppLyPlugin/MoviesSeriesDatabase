package com.applyplugin.moviesseriesdatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.applyplugin.moviesseriesdatabase.databinding.ActivityMainBinding
import com.applyplugin.moviesseriesdatabase.viewmodel.MoviesSeriesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.fragmentContainerView)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.moviesFragment,
                R.id.seriesFragment,
                R.id.watchlistFragment
            )
        )

        binding.bottomNavigation.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

}