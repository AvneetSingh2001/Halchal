package com.avicodes.halchalin.presentation.ui.home.ads

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.databinding.FragmentAdsBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import com.avicodes.halchalin.presentation.ui.home.explore.CommentsAdapter
import com.avicodes.halchalin.presentation.ui.home.home.SliderAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AdsFragment : Fragment() {

    private var _binding: FragmentAdsBinding? = null
    private lateinit var featuredAdapter: SliderAdapter
    private lateinit var viewModel: HomeActivityViewModel

    private lateinit var articleAdapter: ArticlesAdapter

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAdsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            viewModel = (activity as HomeActivity).viewModel

            btnQues.setOnClickListener {
                val action = AdsFragmentDirections.actionAdsFragmentToWriteArticleFragment()
                requireView().findNavController().navigate(action)
            }


            articleAdapter = ArticlesAdapter {
                val action = AdsFragmentDirections.actionAdsFragmentToArticleDetailFragment(it)
                requireView().findNavController().navigate(action)
            }
            binding.rvArticles.adapter = articleAdapter
            binding.rvArticles.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

            viewModel.getAllArticles()

            getFeaturedAds()
            getAllArticles()

        }
    }

    private fun getFeaturedArticles() {
        viewModel.getFeaturedArticles()
        viewModel.featuredArticles.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Result.Error -> {
                    Toast.makeText(context, "An Error Occurred", Toast.LENGTH_LONG).show()
                    Log.e("Error", response.exception?.message.toString())
                }

                is Result.Success -> {
                    response.data?.let {
                        Log.e("Error", it.toString())
                    }
                }

                else -> {
                }
            }
        })
    }

    private fun getAllArticles() {
        viewModel.allArticles.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Result.Error -> {
                    Toast.makeText(context, "An Error Occurred", Toast.LENGTH_LONG).show()
                    Log.e("Avneet Error", response.exception?.message.toString())
                }

                is Result.Success -> {
                    binding.rvArticles.smoothScrollToPosition(0)
                    Log.e("Avneet article", response.data.toString())
                    response.data?.take(8).let {
                        articleAdapter.differ.submitList(it)
                    }
                }

                else -> {
                }
            }
        })
    }

    private fun getFeaturedAds() {
        viewModel.featuredAds.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Result.Error -> {
                    Toast.makeText(context, "An Error Occurred", Toast.LENGTH_LONG).show()
                    Log.e("Error", response.exception?.message.toString())
                }

                is Result.Success -> {
                    response.data?.let {
                        featuredAdapter = SliderAdapter(it)
                        setupFeaturedView()
                    }
                }

                else -> {
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

}