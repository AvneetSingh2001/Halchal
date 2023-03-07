package com.avicodes.halchalin.presentation.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.avicodes.halchalin.MainActivity
import com.avicodes.halchalin.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!


    @Inject
    lateinit var factory : DetailsFragmentViewModelFactory

    lateinit var viewModel: DetailsFragmentViewModel

    val args: DetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity(), factory)[DetailsFragmentViewModel::class.java]


        binding.apply {
            btnContinue.setOnClickListener {
                val name = etName.editText?.text.toString()
                val loc = etLoc.editText?.text.toString()

                if(name != "" && loc != "") {
                    viewModel.saveUser(
                        name = name,
                        loc = loc,
                        phone = args.phone
                    )
                    viewModel.login()
                    navigateToHome()
                } else {
                    if(name == "") {
                        etName.error = "Required"
                    }
                    if(loc == "") {
                        etLoc.error = "Required"
                    }
                }
            }
        }
        return binding.root
    }

    private fun navigateToHome() {
        (activity as MainActivity).moveToHomeActivity()
    }
}