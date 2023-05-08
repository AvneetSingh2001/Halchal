package com.avicodes.halchalin.presentation.ui.home.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.databinding.FragmentCategoryNewsBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import com.avicodes.halchalin.presentation.ui.home.reports.remote.GlobeNewsFragment
import com.avicodes.halchalin.presentation.ui.home.reports.remote.LoaderStateAdapter
import com.avicodes.halchalin.presentation.ui.home.reports.remote.RemoteNewsAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class CategoryNewsFragment : Fragment() {

    private var _binding: FragmentCategoryNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeActivityViewModel
    private lateinit var remoteNewsAdapter: CategoryNewsAdapter
    private lateinit var loaderStateAdapter: LoaderStateAdapter

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


        binding.apply {

            lifecycleScope.launch {
                rvNationalNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (dy < 0) {
                            // Scrolling up
                            if (!btnTop.isVisible)
                                btnTop.visibility = View.VISIBLE
                        } else {
                            // Scrolling down
                            if (btnTop.isVisible)
                                btnTop.visibility = View.GONE
                        }
                    }

                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        when (newState) {

                            AbsListView.OnScrollListener.SCROLL_STATE_FLING -> {
                                // Do something
                                if (btnTop.isVisible) {
                                    lifecycleScope.launch {
                                        delay(100)
                                        btnTop.visibility = View.GONE
                                    }
                                }

                            }

                            AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL -> {
                                // Do something
                                if (btnTop.isVisible) {
                                    lifecycleScope.launch {
                                        delay(100)
                                        btnTop.visibility = View.GONE
                                    }
                                }
                            }

                            else -> {
                                // Do something
                                if (btnTop.isVisible) {
                                    lifecycleScope.launch {
                                        delay(100)
                                        btnTop.visibility = View.GONE
                                    }
                                }
                            }
                        }
                    }
                })
            }

            btnTop.setOnClickListener {
                rvNationalNews.scrollToPosition(0)
            }
        }
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
                    lifecycleScope.launch {
                        delay(2000)
                        hideProgressBar()
                    }
                    remoteNewsAdapter.submitData(it)
                }
            } else {
                viewModel.getCategoryNewsHeadlines(
                    topic = category.name,
                    country = "in",
                    lang = category.lang,
                ).collect {
                    lifecycleScope.launch {
                        delay(2000)
                        hideProgressBar()
                    }
                    remoteNewsAdapter.submitData(it)
                }
            }
        }
    }

    fun setUpNationalRecyclerView() {
        binding.apply {
            remoteNewsAdapter = CategoryNewsAdapter()
            loaderStateAdapter = LoaderStateAdapter { remoteNewsAdapter.retry() }
            rvNationalNews.adapter = remoteNewsAdapter.withLoadStateFooter(loaderStateAdapter)
            rvNationalNews.layoutManager = LinearLayoutManager(activity)
            remoteNewsAdapter.setOnItemClickListener { news ->
                moveToDetailedNews(news)
            }

            refreshLayout.setOnRefreshListener {
                getNews()
                refreshLayout.isRefreshing = false
            }
        }
    }

    private fun moveToDetailedNews(news: NewsRemote) {
        val action =
            CategoryNewsFragmentDirections.actionCategoryNewsFragmentToDetailedRemoteFragment(news)
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