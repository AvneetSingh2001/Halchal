package com.avicodes.halchalin.presentation.ui.auth.providers.phone

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.databinding.FragmentAuthBinding
import com.avicodes.halchalin.presentation.ui.auth.providers.google.GoogleAuthActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AuthFragment : Fragment() {
    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

   private val viewModel by activityViewModels<GoogleAuthActivityViewModel>()

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

        binding.apply {

//            etCountryCode.resetToDefaultCountry()

            btnContinue.setOnClickListener {
                val number = etPhoneNumber.editText?.text.toString()
//                val countryCode = etCountryCode.selectedCountryCode
//                if (validNumber(number) and validCode(countryCode)) {
//                    activity?.let { activity ->
//                        Log.e("Avi", countryCode)
////                        viewModel.authenticatePhone("+$countryCode$number", activity)
////                        observeResults(number)
//                    }
//
//                }
            }

        }
    }


//    fun observeResults(number: String) {
//        binding.apply {
//            lifecycleScope.launch {
//
//                viewModel.phoneState.collectLatest { uiState ->
//
//                    when (uiState) {
//                        is Result.Success -> {
//                            viewModel.phoneState.value = Result.NotInitialized
//                            navigateToCodeAuth("$number")
//                            progCons.visibility = View.INVISIBLE
//                            mainCons.visibility = View.VISIBLE
//                        }
//
//                        is Result.Loading -> {
//                            progCons.visibility = View.VISIBLE
//                            mainCons.visibility = View.INVISIBLE
//                        }
//
//                        is Result.Error -> {
//                            progCons.visibility = View.INVISIBLE
//                            mainCons.visibility = View.VISIBLE
//                            Log.e("AVi", uiState.exception.toString())
//                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
//                        }
//
//                        is Result.NotInitialized -> {}
//                    }
//                }
//
//            }
//        }
//    }

    fun validCode(number: String): Boolean {
        if (number.isNullOrBlank() || number.isEmpty()) {
            binding.etPhoneNumber.error = "Required"
            return false
        }
        return true
    }

    fun validNumber(number: String?): Boolean {
        if (number.isNullOrBlank() || number.isEmpty()) {
            binding.etPhoneNumber.error = "Required"
            return false
        }
        return true
    }

    fun navigateToCodeAuth(phone: String) {
//        val action =
//            AuthFragmentDirections.actionAuthFragmentToCodeAuthFragment("$phone")
//        requireView().findNavController().navigate(action)
    }


}