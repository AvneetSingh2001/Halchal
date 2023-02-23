package com.avicodes.halchalin.presentation.ui.home.explore

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.databinding.FragmentNewsVpBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import com.avicodes.halchalin.presentation.ui.home.reports.local.LocalNewsAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NewsVpFragment : Fragment() {

    private var _binding : FragmentNewsVpBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: NewsViewPagerAdapter
    private lateinit var viewModel: HomeActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNewsVpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as HomeActivity).viewModel
        setUpLocalNewsRecyclerView()
        getNewsList()
        binding.videoViewPager.setPageTransformer(DepthPageTransformer())
    }

    private fun getNewsList() {
        viewModel.localHeadlines.observe(viewLifecycleOwner, Observer {response ->
            when(response) {
                is Result.Error -> {
                    hideProgressBar()
                    Toast.makeText(context,"An Error Occurred", Toast.LENGTH_LONG).show()
                    Log.e("Error", response.exception?.message.toString())
                }

                is Result.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        adapter.differ.submitList(it)
                    }
                }

                else -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun showProgressBar() {
        binding.mainCons.visibility = View.INVISIBLE
    }


    private fun hideProgressBar() {
        binding.mainCons.visibility = View.VISIBLE
    }

    private fun setUpLocalNewsRecyclerView() {
        binding.apply {
            adapter = NewsViewPagerAdapter()
            videoViewPager.adapter = adapter
        }
    }

    class DepthPageTransformer : ViewPager2.PageTransformer {

        private val MIN_SCALE= 0.75F

        override fun transformPage(view: View, position: Float) {
            view.apply {

                val pageWidth = width
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = 0f
                    }
                    position <= 0 -> { // [-1,0]
                        // Use the default slide transition when moving to the left page
                        alpha = 1f
                        translationY = 0f
                        translationZ = 0f
                        scaleY = 1f
                        scaleX = 1f
                    }
                    position <= 1 -> { // (0,1]
                        // Fade the page out.
                        alpha = 1 - position

                        // Counteract the default slide transition
                        translationY = pageWidth * -position
                        // Move it behind the left page
                        translationZ = -1f

                        // Scale the page down (between MIN_SCALE and 1)
                        val scaleFactor = (MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position)))
                        scaleY = scaleFactor
                        scaleX = scaleFactor
                    }
                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = 0f
                    }
                }
            }
        }
    }
}

