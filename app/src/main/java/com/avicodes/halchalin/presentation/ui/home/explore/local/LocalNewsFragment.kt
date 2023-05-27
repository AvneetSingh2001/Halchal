package com.avicodes.halchalin.presentation.ui.home.explore.local

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avicodes.halchalin.MainActivity
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.databinding.FragmentLocalNewsBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import com.avicodes.halchalin.presentation.ui.home.ads.AdsFragment
import com.avicodes.halchalin.presentation.ui.home.reports.remote.RemoteNewsAdapter
import com.avicodes.halchalin.presentation.ui.home.settings.EditFragment
import com.bumptech.glide.Glide
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LocalNewsFragment : Fragment() {

    private var _binding: FragmentLocalNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeActivityViewModel
    private lateinit var localNewsAdapter: LocalNewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLocalNewsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as HomeActivity).viewModel

        setUpLocalNewsRecyclerView()
        viewModel.getLocalNews()
        getNewsList()

        observeLinkCreated()

        localNewsAdapter.setOnItemClickListener {
            navigateToDetailedNews(it)
        }

        localNewsAdapter.setOnCommentClickListener {
            showCommentDialog(it)
        }

        localNewsAdapter.setOnShareClickListener {
            viewModel.createDeepLink(it)
        }

        binding.apply {
            refreshLayout.setOnRefreshListener {
                viewModel.localHeadlines.postValue(Result.Loading(""))
                refreshLayout.isRefreshing = false
                viewModel.updateLocalNews()
            }



            viewModel.curUser.observe(requireActivity(), Observer {
                it?.let {
                    var location = it.location
                    var arr = location.split(", ").toTypedArray()
                    var district = ""
                    if (arr.size == 3) {
                        district = arr[1] + ", " + arr[2]
                    }
                    tvCity.text = district
                }
            })

            infoCons.setOnClickListener {
                rvNationalNews.smoothScrollToPosition(0)
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

        viewModel.sharedNews.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Result.Success -> {
                    hideProgressBar()
                    it.data?.let {news ->
                        navigateToDetailedNews(news)
                        viewModel.sharedNews.postValue(Result.NotInitialized)
                    } ?: Toast.makeText(requireContext(), "No News Found", Toast.LENGTH_SHORT).show()

                }

                is Result.Loading -> {
                    showProgressBar()
                }

                is Result.Error -> {
                    hideProgressBar()
                    Toast.makeText(requireContext(), "No News Found", Toast.LENGTH_SHORT).show()
                }

                is Result.NotInitialized -> {}
            }
        })

    }


    private fun navigateToDetailedNews(news: News) {
        val action = LocalNewsFragmentDirections.actionLocalNewsFragmentToLocalNewsDescFragment(news)
        requireView().findNavController().navigate(action)
    }

    private fun observeLinkCreated() {
        viewModel.linkCreated.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Result.Loading -> {
                }
                is Result.Success -> {
                    it.data?.let { link ->
                        shareLink(link)
                    }
                    viewModel.linkCreated.postValue(Result.NotInitialized)
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), "Error sharing news", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {}
            }
        })
    }

    private fun shareLink(link: String?) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "$link")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share News")
        startActivity(shareIntent)
    }

    private fun showCommentDialog(news: News) {
        val action = LocalNewsFragmentDirections.actionLocalNewsFragmentToCommentFragment(news.newsId)
        requireView().findNavController().navigate(action)
    }

    private fun setUpLocalNewsRecyclerView() {
        binding.apply {
            localNewsAdapter = LocalNewsAdapter()
            rvNationalNews.adapter = localNewsAdapter
            rvNationalNews.layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun getNewsList() {
        viewModel.localHeadlines.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Result.Error -> {
                    hideProgressBar()
                    Toast.makeText(context, "An Error Occurred", Toast.LENGTH_LONG).show()
                    Log.e("Error", response.exception?.message.toString())
                }

                is Result.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        Log.e("Avneet Home Local", it.toString())
                        val news = it
                        localNewsAdapter.differ.submitList(news)
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