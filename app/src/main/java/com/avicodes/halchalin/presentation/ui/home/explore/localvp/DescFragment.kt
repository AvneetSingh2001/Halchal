package com.avicodes.halchalin.presentation.ui.home.explore.localvp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avicodes.halchalin.databinding.FragmentDescBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DescFragment : BottomSheetDialogFragment() {
    private var _binding : FragmentDescBinding? = null
    private val binding get() = _binding!!


    //val args: DescFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDescBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val desc = ""


        binding.apply {
            tvDesc.text = desc
        }
    }
}