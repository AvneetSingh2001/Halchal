package com.avicodes.halchalin.presentation.ui.home.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.snapshots.Snapshot.Companion.observe
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.databinding.FragmentHomeBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeActivityViewModel
    private lateinit var featuredAdapter: SliderAdapter
    private lateinit var latestNewsAdapter: LatestNewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showProgressBar()

        viewModel = (activity as HomeActivity).viewModel

        viewModel.getLocalNews()

        getFeaturedAds()
        getLatestNews()
        Log.e("Initialise Home", "HOme Fragment")

        viewModel.curUser.observe(requireActivity(), Observer {
            binding.etLoc.setText(it?.location.toString())
        })

    }

    private fun getFeaturedAds() {
        viewModel.featuredAds.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Result.Error -> {
                    hideProgressBar()
                    Toast.makeText(context, "An Error Occurred", Toast.LENGTH_LONG).show()
                    Log.e("Error", response.exception?.message.toString())
                }

                is Result.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        featuredAdapter = SliderAdapter(it)
                        setupFeaturedView()
                        hideProgressBar()
                    }
                }

                else -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun setupFeaturedView() {
        binding.apply {
            imageSlider.setSliderAdapter(featuredAdapter)
            imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
            imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
            imageSlider.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH;
            imageSlider.indicatorSelectedColor = Color.WHITE;
            imageSlider.indicatorUnselectedColor = Color.GRAY;
            imageSlider.scrollTimeInSec = 4; //set scroll delay in seconds :
            imageSlider.startAutoCycle();
        }
    }

    private fun getLatestNews() {
        binding.apply {
            latestNewsAdapter = LatestNewsAdapter()
            rvLatestLocal.adapter = latestNewsAdapter
            rvLatestLocal.layoutManager = LinearLayoutManager(activity)
            var latestHeadlines = mutableListOf<String>()
            var one = false
            var two = false
            var three = false

            viewModel.localHeadlines.observe(viewLifecycleOwner, Observer { response ->
                when (response) {
                    is Result.Success -> {
                        response.data?.let {
                            if(it.isNotEmpty()) {
                                it[0].newsHeadline?.let { news -> latestHeadlines.add(news) }
                                one = true
                                if (one and two and three) {
                                    latestNewsAdapter.differ.submitList(latestHeadlines)
                                }
                            }
                        }
                    }
                    else -> {}
                }
            })

            viewModel.nationalHeadlines.observe(viewLifecycleOwner, Observer { response ->
                when (response) {
                    is Result.Success -> {
                        response.data?.let {
                            if (it.results.isNotEmpty())
                                it.results[0].title?.let { news -> latestHeadlines.add(news) }
                            two = true
                            if(one and two and three) {
                                latestNewsAdapter.differ.submitList(latestHeadlines)
                            }
                        }
                    }
                    else -> {}
                }
            })

            viewModel.worldHeadlines.observe(viewLifecycleOwner, Observer { response ->
                when (response) {
                    is Result.Success -> {
                        response.data?.let {
                            if (it.results.isNotEmpty())
                                it.results[0].title?.let { news -> latestHeadlines.add(news) }
                            three = true
                            if(one and two and three) {
                                latestNewsAdapter.differ.submitList(latestHeadlines)
                            }
                        }
                    }
                    else -> {}
                }
            })
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

}