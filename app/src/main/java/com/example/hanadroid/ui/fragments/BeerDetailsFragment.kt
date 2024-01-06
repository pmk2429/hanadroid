package com.example.hanadroid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.hanadroid.databinding.FragmentBeerDetailsBinding
import com.example.hanadroid.databinding.HalfSheetBaseLayoutBinding
import com.example.hanadroid.model.BeerInfo
import com.example.hanadroid.ui.uistate.BeerItemUiState
import com.example.hanadroid.viewmodels.BeerSharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BeerDetailsFragment : Fragment() {

    private var _binding: FragmentBeerDetailsBinding? = null
    private val binding get() = _binding!!

    private val beerSharedViewModel: BeerSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBeerDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindBeerDetailsData(beerSharedViewModel.selectedBeerItem)
    }

    private fun bindBeerDetailsData(selectedBeerItemUiState: StateFlow<BeerItemUiState>) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                selectedBeerItemUiState.collect { uiState ->
                    // open HalfSheet BottomDialog and display details inside
                    displayDetailsHalfSheet(uiState.beerInfo)
                    binding.btnBeerDetails.setOnClickListener { displayDetailsHalfSheet(uiState.beerInfo) }
                }
            }
        }
    }

    private fun displayDetailsHalfSheet(beerInfo: BeerInfo) {
        val halfSheetDialog = BottomSheetDialog(requireContext())
        val dialogBinding = HalfSheetBaseLayoutBinding.inflate(layoutInflater)
        halfSheetDialog.setContentView(dialogBinding.root)
        dialogBinding.apply {
            beerName.text = beerInfo.name
            Glide.with(beerImage)
                .load(beerInfo.imageUrl)
                .fitCenter()
                .into(beerImage)
            beerDescription.text = beerInfo.description
            beerTips.text = beerInfo.brewerTips

            closeButton.setOnClickListener {
                halfSheetDialog.dismiss()
            }
        }
        halfSheetDialog.setCancelable(false)
        halfSheetDialog.show()
    }
}
