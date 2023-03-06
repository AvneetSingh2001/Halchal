package com.avicodes.halchalin.presentation.ui.home.settings

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.fragment.app.findFragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.avicodes.halchalin.R
import com.avicodes.halchalin.databinding.FragmentSettingsBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding : FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeActivityViewModel
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

        viewModel = (activity as HomeActivity).viewModel

        binding.run {
            btnLogout.setOnClickListener {
                (activity as HomeActivity).logout()
            }

            profCons.setOnClickListener {
                navigateToEditScreen()
            }

            viewModel.curUser.observe(requireActivity(), Observer { user->
                user?.let {
                    tvName.text = user.name
                    tvPhone.text = user.mobile

                    Glide.with(ivUser)
                        .load(user.imgUrl).circleCrop()
                        .error(R.drawable.baseline_person_24)
                        .into(ivUser)
                }
            })

            rgLang.setOnCheckedChangeListener { group, checkedId ->
                if(checkedId == R.id.hiLang) {
                    hiLang.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.white)
                    )
                    enLang.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.black)
                    )
                }

                if(checkedId == R.id.enLang) {
                    enLang.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.white)
                    )
                    hiLang.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.black)
                    )
                }
            }
        }
    }

    private fun navigateToEditScreen() {
        val action = SettingsFragmentDirections.actionSettingsFragmentToEditFragment()
        requireView().findNavController().navigate(action)
    }
}