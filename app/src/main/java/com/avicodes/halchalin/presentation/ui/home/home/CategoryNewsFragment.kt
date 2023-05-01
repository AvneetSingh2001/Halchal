package com.avicodes.halchalin.presentation.ui.home.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.databinding.FragmentCategoryNewsBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import com.avicodes.halchalin.presentation.ui.home.reports.remote.GlobeNewsFragment
import com.avicodes.halchalin.presentation.ui.home.reports.remote.RemoteNewsAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CategoryNewsFragment : Fragment() {

    private var _binding: FragmentCategoryNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeActivityViewModel
    private lateinit var remoteNewsAdapter: CategoryNewsAdapter

    val args: CategoryNewsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCategoryNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as HomeActivity).viewModel

        setUpNationalRecyclerView()

        binding.tvContent.text = args.category.name.toString()
        getNews()
    }

    private fun getNews() {
        showProgressBar()
        viewLifecycleOwner.lifecycleScope.launch {
            val category = args.category
            if (category.name == "Tech") {
                viewModel.getCategoryNewsHeadlines(
                    topic = "technology",
                    country = "in",
                    lang = category.lang,
                ).collect {
                    hideProgressBar()
                    remoteNewsAdapter.submitData(it)
                }
            } else {
                viewModel.getCategoryNewsHeadlines(
                    topic = category.name,
                    country = "in",
                    lang = category.lang,
                ).collect {
                    hideProgressBar()
                    remoteNewsAdapter.submitData(it)
                }
            }
        }
    }
    fun setUpNationalRecyclerView() {
        binding.apply {
            remoteNewsAdapter = CategoryNewsAdapter()
            rvNationalNews.adapter = remoteNewsAdapter
            rvNationalNews.layoutManager = LinearLayoutManager(activity)
            remoteNewsAdapter.setOnItemClickListener {news ->
                moveToDetailedNews(news)
            }

            refreshLayout.setOnRefreshListener {
                getNews()
                refreshLayout.isRefreshing = false
            }
        }
    }

    private fun moveToDetailedNews(news: NewsRemote) {
        val action = CategoryNewsFragmentDirections.actionCategoryNewsFragmentToDetailedRemoteFragment(news)
        requireView().findNavController().navigate(action)
    }

    private fun showProgressBar() {
        binding.progCons.visibility = View.VISIBLE
    }


    private fun hideProgressBar() {
        binding.progCons.visibility = View.GONE
    }
}