package com.applyplugin.moviesseriesdatabase.ui.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.applyplugin.moviesseriesdatabase.databinding.MoviesseriesBottomSheetBinding
import com.applyplugin.moviesseriesdatabase.util.Constants.Companion.DEFAULT_SORT
import com.applyplugin.moviesseriesdatabase.util.Constants.Companion.DEFAULT_YEAR
import com.applyplugin.moviesseriesdatabase.util.Constants.Companion.SORT
import com.applyplugin.moviesseriesdatabase.util.Constants.Companion.YEAR
import com.applyplugin.moviesseriesdatabase.viewmodel.MoviesSeriesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.Locale

class MoviesSeriesBottomSheet : BottomSheetDialogFragment() {

    private lateinit var moviesSeriesViewModel: MoviesSeriesViewModel

    private var _binding: MoviesseriesBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val arg by navArgs<MoviesSeriesBottomSheetArgs>()

    private var year = YEAR
    private var year_id = 0
    private var sort = SORT
    private var sort_id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        moviesSeriesViewModel = ViewModelProvider(requireActivity()).get(MoviesSeriesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  MoviesseriesBottomSheetBinding.inflate(inflater, container, false)

        if(moviesSeriesViewModel.appStarted){
            moviesSeriesViewModel.saveMoviesSeriesFilter(DEFAULT_YEAR, 0, DEFAULT_SORT, 0)
        }

        moviesSeriesViewModel.appStarted = false

        moviesSeriesViewModel.readMoviesSeriesFilter.asLiveData().observe(viewLifecycleOwner){ value ->
            year = value.selectedYear
            sort = value.selectedOrder
            updateChips(value.selectedYearId, binding.yearChipGroup)
            updateChips(value.selectedOrderId, binding.orderChipGroup)
        }

        @Suppress("DEPRECATION")
        binding.yearChipGroup.setOnCheckedChangeListener{group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedYear = chip.text.toString().lowercase(Locale.ROOT)
            year = selectedYear
            year_id = selectedChipId
        }

        @Suppress("DEPRECATION")
        binding.orderChipGroup.setOnCheckedChangeListener{group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedOrder = chip.text.toString().lowercase(Locale.ROOT)
            sort = selectedOrder
            sort_id = selectedChipId
        }

        binding.applyButton.setOnClickListener {
            moviesSeriesViewModel.saveMoviesSeriesFilter(
                year,
                year_id,
                sort,
                sort_id
            )

            if(arg.fromFragment == "movies"){
                val action =
                    MoviesSeriesBottomSheetDirections.actionMoviesSeriesBottomSheetToMoviesFragment()
                findNavController().navigate(action)
            }else {
                val action =
                    MoviesSeriesBottomSheetDirections.actionMoviesSeriesBottomSheetToSeriesFragment()
                findNavController().navigate(action)
            }
        }

        return binding.root

    }

    private fun updateChips(chipId: Int, filterChipGroup: ChipGroup) {
        if(chipId != 0){
            try{
                filterChipGroup.findViewById<Chip>(chipId).isChecked = true
            }catch (e: Exception){
                Log.d("MoviesSeriesBottomSheet", e.message.toString())
            }
        }
    }
}