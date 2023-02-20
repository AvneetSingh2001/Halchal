package com.avicodes.halchalin.presentation.ui.home.explore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avicodes.halchalin.R
import com.avicodes.halchalin.databinding.FragmentNewsVpBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NewsVpFragment : Fragment() {

    private var _binding : FragmentNewsVpBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNewsVpBinding.inflate(inflater, container, false)


        return binding.root
    }

}