package com.avicodes.halchalin.presentation.ui.auth.details

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.models.City
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.databinding.FragmentDetailsBinding
import com.avicodes.halchalin.presentation.ui.auth.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!


    @Inject
    lateinit var factory: DetailsFragmentViewModelFactory

    lateinit var viewModel: DetailsFragmentViewModel

    private var citiesList: List<String> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        viewModel =
            ViewModelProvider(requireActivity(), factory)[DetailsFragmentViewModel::class.java]

        getCities()

        binding.apply {

            btnContinue.setOnClickListener {
                val name = etName.editText?.text.toString()
                val loc = etLoc.editText?.text.toString()

                if (name != "" && loc != "") {
                    viewModel.saveUser(
                        name = name,
                        loc = loc,
                    )
                    viewModel.login()
                    navigateToHome()

                } else {
                    if (name == "") {
                        etName.error = "Required"
                    }
                }
            }
        }
        return binding.root
    }

    private fun navigateToHome() {
        (activity as MainActivity).moveToHomeActivity()
    }

    fun getCities() {
        viewModel.getCities().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Result.Success -> {
                    citiesList = it.data?.map(City::name)!!
                    Log.e("Cities Fetch", it.data.toString())
                    val adapter = ArrayAdapter(requireContext(), R.layout.item_city, citiesList)
                    (binding.etLoc.editText as? AutoCompleteTextView)?.setAdapter(adapter)
                }

                is Result.Error -> {
                    Log.e("Cities Fetch", it.exception?.message.toString())
                    val items = listOf("Error Fetching Data")
                    val adapter = ArrayAdapter(requireContext(), R.layout.item_city, items)
                    (binding.etLoc.editText as? AutoCompleteTextView)?.setAdapter(adapter)

                }
                is Result.Loading -> {
                    val items = listOf("Loading...")
                    val adapter = ArrayAdapter(requireContext(), R.layout.item_city, items)
                    (binding.etLoc.editText as? AutoCompleteTextView)?.setAdapter(adapter)
                }
                else -> {}
            }
        })
    }
}