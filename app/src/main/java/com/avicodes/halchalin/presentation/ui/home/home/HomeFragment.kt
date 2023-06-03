package com.avicodes.halchalin.presentation.ui.home.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.databinding.FragmentHomeBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import com.avicodes.halchalin.presentation.ui.home.reports.categories.CategoriesAdapter
import com.bumptech.glide.Glide
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
    private lateinit var adAdapter: SliderAdapter
    private lateinit var latestNewsAdapter: LatestNewsAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

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



        Log.e("Initialise Home", "HOme Fragment")

        binding.apply {

            viewModel.curUser.observe(requireActivity(), Observer {
                it?.let {
                    val loc = it.location.split(", ").toTypedArray()
                    if(loc.size == 2) {
                        tvCity.text = loc[0]
                        tvState.text = loc[1]
                    }
                    Glide.with(ivProfile.context)
                        .load(it.imgUrl)
                        .circleCrop()
                        .into(ivProfile)
                }
            })

            categoriesAdapter = CategoriesAdapter()
            rvCategories.adapter = categoriesAdapter
            rvCategories.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            viewModel.categories.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is Result.Success -> {
                        it.data?.let { categorieslist ->
                            if (categorieslist.isNotEmpty()) {
                                Log.e("Categories", categorieslist.toString())
                                categoriesAdapter.differ.submitList(categorieslist)
                                tv3.visibility = View.VISIBLE
                            }
                        }
                    }

                    is Result.Error -> {
                        Log.e("Categories", "Error")

                        tv3.visibility = View.GONE
                    }

                    else -> {
                        tv3.visibility = View.GONE
                    }
                }
            })

//            categoriesAdapter.setOnItemClickListener {
//                val action = HomeFragmentDirections.actionHomeFragmentToCategoryNewsFragment(it)
//                requireView().findNavController().navigate(action)
//            }
//
//            Glide.with(ivFooter.context)
//                .load(R.drawable.tagline)
//                .into(ivFooter)

            latestNewsAdapter.setOnItemClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToLocalNewsDescFragment(it)
                requireView().findNavController().navigate(action)
            }
        }
    }

    private fun getAds() {
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
                        adAdapter = SliderAdapter(it)
                        setupAdView()
                        hideProgressBar()
                    }
                }

                else -> {
                    showProgressBar()
                }
            }
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

    private fun setupAdView() {
        binding.apply {
            adSlider.setSliderAdapter(adAdapter)
            adSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
            adSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
            adSlider.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH;
            adSlider.indicatorSelectedColor = Color.WHITE;
            adSlider.indicatorUnselectedColor = Color.GRAY;
            adSlider.scrollTimeInSec = 4; //set scroll delay in seconds :
            adSlider.startAutoCycle();
        }
    }

    private fun getLatestNews() {
        binding.apply {
            latestNewsAdapter = LatestNewsAdapter()
            rvLatestLocal.adapter = latestNewsAdapter
            rvLatestLocal.layoutManager = LinearLayoutManager(activity)
            val latestNews = mutableListOf<News>()
            viewModel.localHeadlines.observe(viewLifecycleOwner, Observer { response ->
                when (response) {
                    is Result.Success -> {
                        response.data?.let { newslist ->
                            if (newslist.isNotEmpty()) {
                                val news = newslist[0]
                                latestNews.clear()
                                latestNews.add(0, news)
                                latestNewsAdapter.differ.submitList(latestNews)
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