package com.avicodes.halchalin.presentation.ui.home.reports

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.avicodes.halchalin.databinding.FragmentNewsBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.presentation.ui.home.home.HomeFragmentDirections
import com.avicodes.halchalin.presentation.ui.home.reports.remote.GlobeNewsFragment
import com.avicodes.halchalin.presentation.ui.home.reports.remote.IndiaNewsFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsFragment(
) : Fragment() {


    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: NewsAdapter
    private lateinit var viewModel: HomeActivityViewModel

    // private lateinit var categoriesAdapter: CategoriesAdapter
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
        viewModel.getCategories()
        binding.run {


            tlNews.addTab(
                tlNews.newTab().setText("National News")
            )

            tlNews.addTab(
                tlNews.newTab().setText("International News")
            )




            tlNews.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let { tab ->
                        vpNews.currentItem = tab.position
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })

            addTabsVp()

            GlobeNewsFragment.setOnItemClickListener {
                val action = NewsFragmentDirections.actionNewsFragmentToDetailedRemoteFragment(it)
                requireView().findNavController().navigate(action)
            }

            IndiaNewsFragment.setOnItemClickListener {
                val action = NewsFragmentDirections.actionNewsFragmentToDetailedRemoteFragment(it)
                requireView().findNavController().navigate(action)
            }

            //categoriesAdapter = CategoriesAdapter()
//            rvCategories.adapter = categoriesAdapter
//            rvCategories.layoutManager =
//                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

//            viewModel.categories.observe(viewLifecycleOwner, Observer {
//                when (it) {
//                    is Result.Success -> {
//                        it.data?.let { categorieslist ->
//                            if (categorieslist.isNotEmpty()) {
//                                Log.e("Categories", categorieslist.toString())
//                                categoriesAdapter.differ.submitList(categorieslist)
//                            }
//                        }
//                    }
//
//                    is Result.Error -> {
//                        Log.e("Categories", "Error")
//                    }
//
//                    else -> {
//                    }
//                }
//            })

            addTabsVp()
            vpNews.adapter = adapter

        }
    }

    override fun onResume() {
        super.onResume()
        binding.apply {
            tlNews.selectTab(tlNews.getTabAt(vpNews.currentItem))
        }
    }

    fun addTabsVp() {
        binding.run {
            vpNews.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    refreshLayout.setOnRefreshListener {
                        lifecycleScope.launch {
                            if (position == 0) {
                                viewModel.nationalHeadlines.postValue(Result.NotInitialized)
                                delay(200)
                                viewModel.getNationalNewsHeadlines("national", "in", "hi")
                            } else if (position == 1) {
                                viewModel.worldHeadlines.postValue(Result.NotInitialized)
                                delay(200)
                                viewModel.getInternationalNewsHeadlines(
                                    topic = "world",
                                    country = "in",
                                    lang = "hi"
                                )
                            }
                        }
                        refreshLayout.isRefreshing = false
                    }

                }
            })
        }
    }


    companion object {
        private var onNationalTabClickListener: ((Int) -> Unit)? = null
        fun setOnNationalTabClickListener(listener: (Int) -> Unit) {
            onNationalTabClickListener = listener
        }

        private var onInternationalTabClickListener: ((Int) -> Unit)? = null
        fun setOnInternationalTabClickListener(listener: (Int) -> Unit) {
            onInternationalTabClickListener = listener
        }
    }
}