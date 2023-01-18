package com.avicodes.halchalin.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.avicodes.halchalin.databinding.FragmentAuthBinding
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class AuthFragment : Fragment() {
    private var _binding : FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth : FirebaseAuth
    private lateinit var number : String

    private lateinit var viewModel : AuthFragmentViewModel

    @Inject
    lateinit var viewModelFactory : AuthFragmentViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[AuthFragmentViewModel::class.java]

        binding.apply {
            btnContinue.setOnClickListener {
                if(etPhoneNumber.editText!!.text.isNotEmpty()) {
                    number = etPhoneNumber.editText!!.text.toString()
                    if(number.length == 10) {
                        viewModel.loginUser(number)
                    }
                } else {
                    etPhoneNumber.editText!!.error = "Required"
                }
            }
        }
    }

}