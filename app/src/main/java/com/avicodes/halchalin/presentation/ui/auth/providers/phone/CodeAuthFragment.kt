package com.avicodes.halchalin.presentation.ui.auth.providers.phone

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.databinding.FragmentCodeAuthBinding
import com.avicodes.halchalin.presentation.ui.auth.MainActivity
import com.avicodes.halchalin.presentation.ui.auth.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CodeAuthFragment : Fragment() {

    private var _binding: FragmentCodeAuthBinding? = null
    private val binding get() = _binding!!


    private val viewModel by activityViewModels<MainActivityViewModel>()


    var otpTry: Int = 3

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

        binding.apply {

            btnContinue.setOnClickListener {
                val code = etOtp.editText?.text.toString()
                if (validNumber(code)) {
                    activity?.let { activity ->
                        viewModel.verifyOtp(code, activity)
                        observeState()
                    }
                }
            }


            btnReqestAgain.setOnClickListener {
                //Log.e("Requested Again", args.phone)
                navigateBack()
            }

            viewModel.curUser.observe(viewLifecycleOwner, Observer {
//                if (it.userId != args.phone) {
//                    checkRemote()
//                } else {
//                    Log.e("CodeAuth", "Navigate To Home")
//                    viewModel.login()
//                    navigateToHomeScreen()
//                }
            })

        }
    }


    fun observeState() {

        binding.apply {
            lifecycleScope.launch {
                viewModel.codeState.collectLatest { uiState ->
                    when (uiState) {
                        is Result.Success -> {
                            viewModel.codeState.value = Result.NotInitialized
                            progCons.visibility = View.INVISIBLE
                            mainCons.visibility = View.VISIBLE
                            navigateToNextScreen()
                        }

                        is Result.Loading -> {
                            progCons.visibility = View.VISIBLE
                            mainCons.visibility = View.INVISIBLE
                        }

                        is Result.Error -> {
                            progCons.visibility = View.INVISIBLE
                            mainCons.visibility = View.VISIBLE
                            val text = (uiState as Result.Error).exception?.message
                            if (text == context?.getString(R.string.invalid_code)) {
                                binding.etOtp.error = "Wrong OTP"
                            }
                            otpTry--
                            if(otpTry == 0) {
                                navigateBack()
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

    fun navigateBack() {
//        val action = CodeAuthFragmentDirections.actionCodeAuthFragmentToAuthFragment()
//        requireView().findNavController().navigate(action)
    }

    fun navigateToNextScreen() {
        viewModel.getCurrentUser()
    }


    private fun checkRemote() {
        viewModel.getUser("io").observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Success -> {
                    if (result.data == null) {
                        navigateToDetailsScreen()
                    } else if (result.data.userId == "fd") {
                        viewModel.saveUser(
                            result.data.userId,
                            result.data.name,
                            result.data.location,
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

    fun validNumber(number: String?): Boolean {
        if (number.isNullOrBlank() || number.isEmpty()) {
            binding.etOtp.error = "Required"
            return false
        } else if (number.length != 6) {
            binding.etOtp.error = "Invalid"
            return false
        }
        return true
    }

    fun navigateToDetailsScreen() {
//        val action = CodeAuthFragmentDirections.actionCodeAuthFragmentToDetailsFragment(args.phone)
//        requireView().findNavController().navigate(action)
    }

    fun navigateToHomeScreen() {
        (activity as MainActivity).moveToHomeActivity()
    }

}