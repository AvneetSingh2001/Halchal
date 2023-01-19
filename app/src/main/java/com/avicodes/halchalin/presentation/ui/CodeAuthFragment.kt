package com.avicodes.halchalin.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.utils.Response
import com.avicodes.halchalin.databinding.FragmentCodeAuthBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
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
                }
            }

            btnReqestAgain.setOnClickListener {

            }
        }

    }

}