package com.example.hanadroid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hanadroid.adapters.ImageCarouselAdapter
import com.example.hanadroid.databinding.FragmentLoadImageBinding
import com.example.hanadroid.repository.HanaImagesRepository
import kotlinx.coroutines.launch

class LoadImageFragment : Fragment() {

    private var _binding: FragmentLoadImageBinding? = null
    private val binding get() = _binding!!

    private lateinit var carouselAdapter: ImageCarouselAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadImageBinding.inflate(inflater, container, false)
        carouselAdapter = ImageCarouselAdapter(HanaImagesRepository.hanaImageList)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                binding.recyclerViewImageCarousel.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    addItemDecoration(
                        DividerItemDecoration(
                            context,
                            DividerItemDecoration.VERTICAL
                        )
                    )
                    adapter = carouselAdapter
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*
    imageView.setImageBitmap(
        decodeSampledBitmapFromResource(resources, R.id.myimage, 100, 100)
     )
     */

}