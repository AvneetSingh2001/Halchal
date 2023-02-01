package com.avicodes.halchalin.presentation.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.utils.Response
import com.avicodes.halchalin.databinding.FragmentCodeAuthBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class CodeAuthFragment : Fragment() {

    private var _binding: FragmentCodeAuthBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel : AuthFragmentViewModel
    @Inject
    lateinit var viewModelFactory : AuthFragmentViewModelFactory

    val args: CodeAuthFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCodeAuthBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[AuthFragmentViewModel::class.java]

        binding.apply {


            btnContinue.setOnClickListener {
                if(etOtp.editText?.text.isNullOrEmpty()) {
                    etOtp.error = "Required"
                } else if(etOtp.editText?.text?.length != 6){
                    etOtp.error = "Invalid"
                } else {
                    val code = etOtp.editText!!.text.toString()
                    viewModel.verifyOtp(code)
                    progCons.visibility = View.VISIBLE
                    mainCons.visibility = View.INVISIBLE
                    lifecycleScope.launch {
                        viewModel.signUpState.collectLatest {uiState ->
                            when(uiState) {
                                is Response.Success -> {
                                    progCons.visibility = View.INVISIBLE
                                    mainCons.visibility = View.VISIBLE
                                    val action = CodeAuthFragmentDirections.actionCodeAuthFragmentToDetailsFragment()
                                    requireView().findNavController().navigate(action)
                                }

                                is Response.Loading -> {
                                    val text = (uiState as Response.Loading).message
                                    Log.e("loading", text.toString())

                                    if(text == context?.getString(R.string.code_sent)) {
                                        progCons.visibility = View.INVISIBLE
                                        mainCons.visibility = View.VISIBLE
                                    }
                                    Log.e("MYTAG", "codeAuth")

                                }

                                is Response.Error -> {
                                    progCons.visibility = View.INVISIBLE
                                    mainCons.visibility = View.VISIBLE
                                    val text = (uiState as Response.Error).exception?.message
                                    if (text == context?.getString(R.string.invalid_code)) {
                                        requireView().findNavController().popBackStack()
                                    }
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

            btnReqestAgain.setOnClickListener {
                Log.e("Requested Again", args.phone)
                requireView().findNavController().popBackStack()
            }

        }

    }

}