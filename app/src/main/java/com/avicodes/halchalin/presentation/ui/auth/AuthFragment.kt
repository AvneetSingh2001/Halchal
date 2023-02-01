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
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.utils.Response
import com.avicodes.halchalin.databinding.FragmentAuthBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class AuthFragment : Fragment() {
    private var _binding : FragmentAuthBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel : AuthFragmentViewModel

    @Inject
    lateinit var viewModelFactory : AuthFragmentViewModelFactory

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
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[AuthFragmentViewModel::class.java]

        binding.apply {
            btnContinue.setOnClickListener {
                if(etPhoneNumber.editText?.text.isNullOrEmpty()) {
                    etPhoneNumber.error = "Required"
                } else if(etPhoneNumber.editText?.text?.length != 10){
                    etPhoneNumber.error = "Invalid"
                } else {
                    val phone = etPhoneNumber.editText!!.text.toString()
                    viewModel.authenticatePhone(phone)
                    lifecycleScope.launch {
                        viewModel.signUpState.collectLatest {uiState ->
                            when(uiState) {
                                is Response.Success -> {
                                    progCons.visibility = View.INVISIBLE
                                    mainCons.visibility = View.VISIBLE
                                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                                }

                                is Response.Loading -> {
                                    val text = (uiState as Response.Loading).message
                                    progCons.visibility = View.VISIBLE
                                    mainCons.visibility = View.INVISIBLE
                                    if(text == context?.getString(R.string.code_sent)) {
                                        val action =
                                            AuthFragmentDirections.actionAuthFragmentToCodeAuthFragment("+91$phone")
                                        requireView().findNavController().navigate(action)
                                    }
                                    Log.e("MYTAG", "AuthFragment")
                                }

                                is Response.Error -> {
                                    progCons.visibility = View.INVISIBLE
                                    mainCons.visibility = View.VISIBLE
                                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()

                                }

                                is Response.NotInitialized -> {
                                    progCons.visibility = View.INVISIBLE
                                    mainCons.visibility = View.VISIBLE
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
        if(auth.currentUser != null) {
            binding.progCons.visibility = View.VISIBLE
            binding.mainCons.visibility = View.INVISIBLE
            auth.currentUser!!.phoneNumber?.let { navigateToNextScreen(it) }
        }
    }


    fun navigateToNextScreen(phone: String) {
        viewModel.getUser(phone).observe(requireActivity(), Observer {
            when(it) {
                is Response.Success -> {
                    if(it.data != null) {
                        Log.i("MYTAG", "Success to home: ${phone}")
                        navigateToHomeScreen()
                    } else {
                        Log.i("MYTAG", "Success to details: ${phone}")
                        navigateToDetailsScreen()
                    }
                }

                is Response.Error -> {
                    Log.e("Error", it.exception.toString())
                }
                else -> {
                        Log.e("Loading", it.toString())
                }
            }
        })
    }

    fun navigateToDetailsScreen() {
        val action = AuthFragmentDirections.actionAuthFragmentToHomeActivity()
        requireView().findNavController().navigate(action)
    }

    fun navigateToHomeScreen() {
        val action = AuthFragmentDirections.actionAuthFragmentToHomeActivity()
        requireView().findNavController().navigate(action)
    }
}