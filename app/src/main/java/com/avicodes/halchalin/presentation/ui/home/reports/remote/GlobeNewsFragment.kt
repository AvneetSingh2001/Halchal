package com.avicodes.halchalin.presentation.ui.home.reports.remote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.databinding.FragmentGlobeNewsBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import com.avicodes.halchalin.presentation.ui.home.reports.LoaderStateAdapter
import com.avicodes.halchalin.presentation.ui.home.reports.NewsFragment
import com.avicodes.halchalin.presentation.ui.home.reports.RemoteNewsAdapter
import kotlinx.coroutines.delay
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
        showProgressBar()
        setUpNationalRecyclerView()
        getNews()

        NewsFragment.setOnInternationalTabClickListener {
            binding.rvNationalNews.smoothScrollToPosition(it)
        }


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

    fun getNews() {
        lifecycleScope.launch {
            viewModel.worldHeadlines.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is Result.Success -> {
                        lifecycleScope.launch {
                            delay(2000)
                            hideProgressBar()
                        }
                        it.data?.let { it2 ->
                            lifecycleScope.launch {
                                remoteNewsAdapter.submitData(it2)
                            }
                        }

                    }

                    is Result.NotInitialized -> {
                        showProgressBar()
                    }

                    else -> {
                        showProgressBar()
                    }
                }
            })
        }
    }

    fun setUpNationalRecyclerView() {
        binding.apply {
            remoteNewsAdapter = RemoteNewsAdapter()
            loaderStateAdapter = LoaderStateAdapter { remoteNewsAdapter.retry() }
            rvNationalNews.adapter = remoteNewsAdapter.withLoadStateFooter(loaderStateAdapter)
            rvNationalNews.layoutManager = LinearLayoutManager(activity)
            remoteNewsAdapter.setOnItemClickListener { news ->
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