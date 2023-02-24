package com.avicodes.halchalin.presentation.ui.home.reports.remote

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.avicodes.halchalin.databinding.FragmentIndiaNewsBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import com.avicodes.halchalin.data.utils.Result

class IndiaNewsFragment : Fragment() {

    private var _binding: FragmentIndiaNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeActivityViewModel
    private lateinit var remoteNewsAdapter: RemoteNewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentIndiaNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as HomeActivity).viewModel

        setUpNationalRecyclerView()

        getNewsList()

    }

    fun setUpNationalRecyclerView() {
        binding.apply {
            remoteNewsAdapter = RemoteNewsAdapter()
            rvNationalNews.adapter = remoteNewsAdapter
            rvNationalNews.layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun getNewsList() {
        viewModel.nationalHeadlines.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Result.Error -> {
                    hideProgressBar()
                    Toast.makeText(context, "An Error Occurred", Toast.LENGTH_LONG).show()
                    Log.e("Error", response.exception?.message.toString())
                }

                is Result.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        remoteNewsAdapter.differ.submitList(it.results)
                    }
                }

                else -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun showProgressBar() {
        binding.progCons.visibility = View.VISIBLE
        binding.mainCons.visibility = View.INVISIBLE
    }


    private fun hideProgressBar() {
        binding.progCons.visibility = View.GONE
        binding.mainCons.visibility = View.VISIBLE
    }

}