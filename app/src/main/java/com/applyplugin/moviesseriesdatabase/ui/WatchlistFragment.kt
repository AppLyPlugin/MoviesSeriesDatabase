package com.applyplugin.moviesseriesdatabase.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applyplugin.moviesseriesdatabase.MainViewModel
import com.applyplugin.moviesseriesdatabase.R
import com.applyplugin.moviesseriesdatabase.adapter.WatchlistAdapter
import com.applyplugin.moviesseriesdatabase.databinding.WatchlistFragmentBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WatchlistFragment : Fragment() {

    private val mAdapter: WatchlistAdapter by lazy { WatchlistAdapter(requireActivity(), mainViewModel) }
    private val mainViewModel: MainViewModel by viewModels()

    private var _binding: WatchlistFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = WatchlistFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.mAdapter = mAdapter

        setUpRecyclerView(binding.recyclerview)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.delete_all_watchlist, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == android.R.id.home) {
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                }else if(menuItem.itemId == R.id.delete_all_watchlist){
                    mainViewModel.deleteAllWatchlist()
                    showDeleteAllSnackBar()
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        mainViewModel.readWatchlist.observe(viewLifecycleOwner) { watchlistEntity ->
            mAdapter.setData(watchlistEntity)
        }

        return binding.root

    }

    private fun setUpRecyclerView(recyclerview: RecyclerView) {
        recyclerview.adapter = mAdapter
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
    }


    private fun showDeleteAllSnackBar() {
        Snackbar.make(
            binding.root,
            "All Watchlist Deleted",
            Snackbar.LENGTH_SHORT
        ).setAction("OK"){}
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mAdapter.clearContextualActionMode()
        _binding = null
    }
}