package com.example.hanadroid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hanadroid.R
import com.example.hanadroid.databinding.FragmentLoadImageBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoadImageFragment : Fragment() {

    private var _binding: FragmentLoadImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_loadImage_to_loadVideoGif)
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