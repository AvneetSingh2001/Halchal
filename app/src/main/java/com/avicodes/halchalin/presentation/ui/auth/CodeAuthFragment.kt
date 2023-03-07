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
import androidx.navigation.fragment.navArgs
import com.avicodes.halchalin.MainActivity
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.databinding.FragmentCodeAuthBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class CodeAuthFragment : Fragment() {

    private var _binding: FragmentCodeAuthBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AuthFragmentViewModel

    @Inject
    lateinit var viewModelFactory: AuthFragmentViewModelFactory

    @Inject
    lateinit var auth: FirebaseAuth

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

        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[AuthFragmentViewModel::class.java]

        binding.apply {
            btnContinue.setOnClickListener {
                if (etOtp.editText?.text.isNullOrEmpty()) {
                    etOtp.error = "Required"
                } else if (etOtp.editText?.text?.length != 6) {
                    etOtp.error = "Invalid"
                } else {
                    val code = etOtp.editText!!.text.toString()
                    viewModel.verifyOtp(code)
                    progCons.visibility = View.VISIBLE
                    mainCons.visibility = View.INVISIBLE
                    lifecycleScope.launch {
                        viewModel.signUpState.collectLatest { uiState ->
                            when (uiState) {
                                is Result.Success -> {
                                    progCons.visibility = View.INVISIBLE
                                    mainCons.visibility = View.VISIBLE
                                    navigateToNextScreen()
                                }

                                is Result.Loading -> {
                                    val text = (uiState as Result.Loading).message
                                    Log.e("loading", text.toString())

                                    if (text == context?.getString(R.string.code_sent)) {
                                        progCons.visibility = View.INVISIBLE
                                        mainCons.visibility = View.VISIBLE
                                    }
                                    Log.e("MYTAG", "codeAuth")

                                }

                                is Result.Error -> {
                                    progCons.visibility = View.INVISIBLE
                                    mainCons.visibility = View.VISIBLE
                                    val text = (uiState as Result.Error).exception?.message
                                    if (text == context?.getString(R.string.invalid_code)) {
                                        requireView().findNavController().popBackStack()
                                    }
                                }
                                is Result.NotInitialized -> {
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

            viewModel.curUser.observe(viewLifecycleOwner, Observer {
                if (it.userId != args.phone) {
                    checkRemote()
                } else {
                    Log.e("CodeAuth", "Navigate To Home")
                    viewModel.login()
                    navigateToHomeScreen()
                }
            })

        }
    }

    fun navigateToNextScreen() {
        viewModel.getCurrentUser()
    }


    private fun checkRemote() {
        viewModel.getUser(args.phone).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Success -> {
                    if (result.data == null) {
                        navigateToDetailsScreen()
                    } else if (result.data.userId == args.phone) {
                        viewModel.saveUser(
                            result.data.userId,
                            result.data.name,
                            result.data.location,
                            result.data.mobile,
                            result.data.imgUrl,
                            result.data.about
                        )
                    } else {
                        navigateToDetailsScreen()
                    }
                }

                is Result.Loading -> {
                    binding.progCons.visibility = View.INVISIBLE
                    binding.mainCons.visibility = View.VISIBLE
                }

                is Result.Error -> {
                    binding.progCons.visibility = View.INVISIBLE
                    binding.mainCons.visibility = View.VISIBLE
                    Log.e("CodeFragment", result.exception?.message.toString())
                    Toast.makeText(requireActivity(), "Try Again", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        })
    }

    fun navigateToDetailsScreen() {
        val action = CodeAuthFragmentDirections.actionCodeAuthFragmentToDetailsFragment(args.phone)
        requireView().findNavController().navigate(action)
    }

    fun navigateToHomeScreen() {
        (activity as MainActivity).moveToHomeActivity()
    }

}