package com.avicodes.halchalin.presentation.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.avicodes.halchalin.MainActivity
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.databinding.FragmentAuthBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class AuthFragment : Fragment() {
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AuthFragmentViewModel

    @Inject
    lateinit var viewModelFactory: AuthFragmentViewModelFactory

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAuthBinding.inflate(inflater, container, false)

        binding.progCons.visibility = View.VISIBLE
        binding.mainCons.visibility = View.INVISIBLE

        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[AuthFragmentViewModel::class.java]

        viewModel.isLoggedIn().observe(viewLifecycleOwner, Observer {
            Log.e("AuthFragment", "Is Logged in: ${it.toString()}")
            if (it == true) {
                navigateToHomeScreen()
            } else {
                binding.progCons.visibility = View.INVISIBLE
                binding.mainCons.visibility = View.VISIBLE
            }
        })

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {
            btnContinue.setOnClickListener {
                if (etPhoneNumber.editText?.text.isNullOrEmpty()) {
                    etPhoneNumber.error = "Required"
                } else if (etPhoneNumber.editText?.text?.length != 10) {
                    etPhoneNumber.error = "Invalid"
                } else {
                    val phone = etPhoneNumber.editText!!.text.toString()
                    viewModel.authenticatePhone(phone)
                    lifecycleScope.launch {
                        viewModel.signUpState.collectLatest { uiState ->
                            when (uiState) {
                                is Result.Success -> {
                                    progCons.visibility = View.INVISIBLE
                                    mainCons.visibility = View.VISIBLE
                                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                                }

                                is Result.Loading -> {
                                    val text = (uiState as Result.Loading).message
                                    progCons.visibility = View.VISIBLE
                                    mainCons.visibility = View.INVISIBLE
                                    if (text == context?.getString(R.string.code_sent)) {
                                        navigateToCodeAuth(phone)
                                    }
                                    Log.e("MYTAG", "AuthFragment")
                                }

                                is Result.Error -> {
                                    progCons.visibility = View.INVISIBLE
                                    mainCons.visibility = View.VISIBLE
                                    Log.e("AuthFragment", uiState.exception?.message.toString())
                                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                                }

                                is Result.NotInitialized -> {
                                }
                            }
                        }
                    }

                }
            }



        }
    }



    override fun onStart() {
        super.onStart()
    }

    fun navigateToHomeScreen() {
        (activity as MainActivity).moveToHomeActivity()
    }

    fun navigateToCodeAuth(phone: String) {
        val action =
            AuthFragmentDirections.actionAuthFragmentToCodeAuthFragment("$phone")
        requireView().findNavController().navigate(action)
    }
}