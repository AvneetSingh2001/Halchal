package com.avicodes.halchalin.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.utils.Response
import com.avicodes.halchalin.databinding.FragmentAuthBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
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
                                        //VIEW OTP EditText
                                        val action =
                                            AuthFragmentDirections.actionAuthFragmentToCodeAuthFragment()
                                        requireView().findNavController().navigate(action)

                                    }
                                }

                                is Response.Error -> {

                                    progCons.visibility = View.INVISIBLE
                                    mainCons.visibility = View.VISIBLE

                                    val text = (uiState as Response.Error).exception?.message
                                    if(text == context?.getString(R.string.invalid_code)) {
                                        // show only OTP ET
                                    } else {
                                        // show phone number ET
                                    }
                                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                                }

                                is Response.NotInitialized -> {
                                    //show phone number screen
                                    // no errors
                                }
                            }
                        }
                    }

                }
            }
        }
    }

}