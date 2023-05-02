package com.avicodes.halchalin.presentation.ui.home.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
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


class CategoryNewsFragment : Fragment() {

    private var _binding: FragmentCategoryNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeActivityViewModel
    private lateinit var remoteNewsAdapter: RemoteNewsAdapter

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

        val category = args.category
        viewModel = (activity as HomeActivity).viewModel
        binding.apply {
            tvContent.text = category.name
        }
        if(category.name == "Tech"){
            viewModel.getCategoryNewsHeadlines(
                topic = "technology",
                country = "in",
                lang = category.lang,
                page = null
            )
        } else {
            viewModel.getCategoryNewsHeadlines(
                topic = category.name,
                country = "in",
                lang = category.lang,
                page = null
            )
        }
        setUpNationalRecyclerView()
        observeNewsList()
    }

    fun observeNewsList() {
        viewModel.categoryHeadlines.observe(viewLifecycleOwner, Observer {response ->
            when(response) {
                is Result.Error -> {
                    hideProgressBar()
                    Toast.makeText(context,"An Error Occured", Toast.LENGTH_LONG).show()
                }

                is Result.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        Log.e("Avneet", it.results.toString())
                        remoteNewsAdapter.differ.submitList(it.results)
                        viewModel.categoryHeadlines.postValue(Result.NotInitialized)
                    }
                }
                is Result.NotInitialized -> {}
                else -> {
                    Log.i("Avneet", "Data Loading")
                    showProgressBar()
                }
            }
        })
    }

    fun setUpNationalRecyclerView() {
        binding.apply {
            remoteNewsAdapter = RemoteNewsAdapter()
            rvNationalNews.adapter = remoteNewsAdapter
            rvNationalNews.layoutManager = LinearLayoutManager(activity)
            remoteNewsAdapter.setOnItemClickListener {news ->
                moveToDetailedNews(news)
            }


            refreshLayout.setOnRefreshListener {
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
        binding.mainCons.visibility = View.INVISIBLE
    }


    private fun hideProgressBar() {
        binding.progCons.visibility = View.GONE
        binding.mainCons.visibility = View.VISIBLE
    }
}