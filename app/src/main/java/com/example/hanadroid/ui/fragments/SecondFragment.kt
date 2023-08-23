package com.example.hanadroid.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.hanadroid.databinding.FragmentSecondBinding
import com.example.hanadroid.viewmodels.UniversitySharedViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: UniversitySharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        val universityName = arguments?.getString(UniversityListFragment.ARGS_UNIVERSITY_NAME)
        Log.i("~!@# BUNDLE - ", "$universityName")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.universityLiveData.observe(viewLifecycleOwner) {
            Log.i("~!@#", "University --- ${it.name}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
