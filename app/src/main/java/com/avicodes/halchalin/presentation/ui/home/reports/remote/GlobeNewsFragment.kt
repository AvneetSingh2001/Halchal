package com.avicodes.halchalin.presentation.ui.home.reports.remote

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.databinding.FragmentGlobeNewsBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class GlobeNewsFragment : Fragment() {
    private var _binding: FragmentGlobeNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeActivityViewModel
    private lateinit var remoteNewsAdapter: RemoteNewsAdapter
    private lateinit var loaderStateAdapter: LoaderStateAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGlobeNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as HomeActivity).viewModel

        setUpNationalRecyclerView()
        getNews()
    }

    fun getNews() {
        showProgressBar()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getInternationalNewsHeadlines(
                topic = "world",
                country = "in",
                lang = "hi"
            ).collect {
                remoteNewsAdapter.submitData(it)
            }
        }
        hideProgressBar()
    }

    fun setUpNationalRecyclerView() {
        binding.apply {
            remoteNewsAdapter = RemoteNewsAdapter()
            loaderStateAdapter = LoaderStateAdapter { remoteNewsAdapter.retry() }
            rvNationalNews.adapter = remoteNewsAdapter.withLoadStateFooter(loaderStateAdapter)
            rvNationalNews.layoutManager = LinearLayoutManager(activity)
            remoteNewsAdapter.setOnItemClickListener {news ->
                onItemClickListener?.let {
                    it(news)
                }
            }
        }
    }

    private fun showProgressBar() {
        binding.progCons.visibility = View.VISIBLE
        binding.mainCons.visibility = View.INVISIBLE
    }


    private fun hideProgressBar() {
        binding.progCons.visibility = View.GONE
        binding.mainCons.visibility = View.VISIBLE
    }



    companion object {
        private var onItemClickListener: ((NewsRemote) -> Unit)? = null
        fun setOnItemClickListener(listener: (NewsRemote) -> Unit) {
            onItemClickListener = listener
        }
    }

}