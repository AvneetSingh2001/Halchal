package com.avicodes.halchalin.presentation.ui.home.explore

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.avicodes.halchalin.R
import com.avicodes.halchalin.databinding.FragmentDescBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DescFragment : BottomSheetDialogFragment() {
    private var _binding : FragmentDescBinding? = null
    private val binding get() = _binding!!


    val args: DescFragmentArgs by navArgs()

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

        val desc = args.desc


        binding.apply {
            tvDesc.text = desc
        }
    }
}