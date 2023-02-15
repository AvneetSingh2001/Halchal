package com.avicodes.halchalin.presentation.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avicodes.halchalin.R
import com.avicodes.halchalin.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding : FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            btnLogout.setOnClickListener {
                (activity as HomeActivity).logout()
            }
        }
    }
}