package com.avicodes.halchalin.presentation.ui.home.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.databinding.FragmentNewsBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import com.avicodes.halchalin.data.utils.Result

@AndroidEntryPoint
class NewsFragment(
) : Fragment() {


    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: NewsAdapter
    private lateinit var viewModel: HomeActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = NewsAdapter(childFragmentManager, lifecycle)
        viewModel = (activity as HomeActivity).viewModel

        observeUser()

        binding.run {

            tlNews.addTab(
                tlNews.newTab().setText("Local")
            )

            tlNews.addTab(
                tlNews.newTab().setText("India")
            )

            tlNews.addTab(
                tlNews.newTab().setText("Global")
            )

            vpNews.adapter = adapter

            tlNews.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        vpNews.currentItem = tab.position
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}

            })

            addTabsVp()
        }
    }

    fun addTabsVp() {
        binding.run {
            vpNews.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tlNews.selectTab(tlNews.getTabAt(position))

                    refreshLayout.setOnRefreshListener {
                        refreshLayout.isRefreshing = false
                        if (position == 0) {
                            viewModel.getLocalNews()
                        } else if (position == 1) {
                            viewModel.getNationalNewsHeadlines("in", "hi")
                        } else {
                            viewModel.getWorldNewsHeadlines("world", "in", "hi")
                        }
                    }

                }
            })
        }
    }

    private fun observeUser() {
        viewModel.curUser.observe(requireActivity(), Observer {
            viewModel.getLocalNews()
            addTabsVp()
        })
    }
}