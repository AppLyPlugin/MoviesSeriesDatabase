package com.applyplugin.moviesseriesdatabase.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.applyplugin.moviesseriesdatabase.viewmodel.MainViewModel
import com.applyplugin.moviesseriesdatabase.R
import com.applyplugin.moviesseriesdatabase.adapter.SeriesAdapter
import com.applyplugin.moviesseriesdatabase.databinding.SeriesFragmentBinding
import com.applyplugin.moviesseriesdatabase.util.NetworkListener
import com.applyplugin.moviesseriesdatabase.util.NetworkResult
import com.applyplugin.moviesseriesdatabase.viewmodel.MoviesSeriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class SeriesFragment : Fragment(), SearchView.OnQueryTextListener {

    private val mAdapter: SeriesAdapter by lazy { SeriesAdapter() }
    private val mainViewModel: MainViewModel by viewModels()
    private val moviesSeriesViewModel: MoviesSeriesViewModel by viewModels()

    private var _binding: SeriesFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var networkListener: NetworkListener

    private var startOnCreate by Delegates.notNull<Boolean>()
    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = SeriesFragmentBinding.inflate(inflater, container, false)

        setUpRecyclerView()

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu)

                val search = menu.findItem(R.id.menu_search)
                val searchView = (search.actionView as? SearchView)!!
                searchView.queryHint = getString(R.string.series_search_hint)
                searchView.isSubmitButtonEnabled = true
                searchView.setOnQueryTextListener(this@SeriesFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)


        startOnCreate = true
        requestApiData()

        moviesSeriesViewModel.readBackOnline.observe(viewLifecycleOwner) {
            moviesSeriesViewModel.backOnline = it
        }

        job = lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    moviesSeriesViewModel.networkStatus = status
                    if (!startOnCreate) {
                        moviesSeriesViewModel.showNetworkStatus()
                        if (status) {
                            requestApiData()
                        } else {
                            loadDataFromCache()
                        }
                    }
                }

        }

        binding.floatingActionButton.setOnClickListener {
            if (moviesSeriesViewModel.networkStatus) {
                val action =
                    SeriesFragmentDirections.actionSeriesFragmentToMoviesSeriesBottomSheet("series")
                findNavController().navigate(action)
            } else {
                moviesSeriesViewModel.showNetworkStatus()
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            startOnCreate = true
            requestApiData()
            binding.swipeRefresh.isRefreshing = false;
        }

        startOnCreate = false

        return binding.root
    }

    private fun requestApiData() {
        mainViewModel.getSeries(moviesSeriesViewModel.applySeriesQuery())
        mainViewModel.moviesseriesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it.moviesseriesResult!!) }
                }

                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }

                else -> {}
            }
        }
    }

    private fun searchApiData(searchIds: String) {
        showShimmerEffect()
        mainViewModel.searchSeries(moviesSeriesViewModel.applySeriesSearchQuery(searchIds))
        mainViewModel.moviesseriesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    val seriesData = response.data
                    seriesData?.let {
                        mAdapter.setData(it.moviesseriesResult!!)
                    }
                }

                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }

                else -> {}
            }

        }
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readSeriesData.observe(viewLifecycleOwner) { database ->
                try {
                    mAdapter.setData(database[0].series.moviesseriesResult!!)
                } catch (e: Exception) {
                    binding.errorImg.visibility = View.VISIBLE;
                    binding.errorTxt.visibility = View.VISIBLE;
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.recyclerview.adapter = mAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun showShimmerEffect() {
        binding.fragmentShimmer.startShimmer()
        binding.recyclerview.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        binding.fragmentShimmer.stopShimmer()
        binding.fragmentShimmer.visibility = View.GONE
        binding.recyclerview.visibility = View.VISIBLE
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchApiData(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    override fun onPause() {
        super.onPause()
        job?.cancel()
        job = null
    }

}