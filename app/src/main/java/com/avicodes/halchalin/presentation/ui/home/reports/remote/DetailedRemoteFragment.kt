package com.avicodes.halchalin.presentation.ui.home.reports.remote

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
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
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.databinding.FragmentDetailedRemoteBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import com.avicodes.halchalin.presentation.ui.home.explore.AdsAdapter
import com.avicodes.halchalin.presentation.ui.home.explore.NewsResAdapter
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView


class DetailedRemoteFragment : Fragment() {
    private var _binding: FragmentDetailedRemoteBinding? = null
    private val binding get() = _binding!!

    val args: DetailedRemoteFragmentArgs by navArgs()
    private lateinit var viewModel: HomeActivityViewModel

    private lateinit var adAdapter: AdsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailedRemoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as HomeActivity).viewModel
        val news = args.news

        binding.apply {
            tvHeadline.text = news.title

            if (news.content != null) {
                tvDesc.text = news.content
            } else if (news.description != null) {
                tvDesc.text = news.description
            } else {
                tvDesc.text = news.title
            }

            adAdapter = AdsAdapter()
            binding.rvAds.adapter = adAdapter
            binding.rvAds.layoutManager = LinearLayoutManager(activity)
            getNewsAds()

            observeTopAds()

            Glide.with(ivNews.context)
                .load(news.image_url)
                .error(R.drawable.halchal_logo_2)
                .into(ivNews)

            tvSource.text = news.source_id

            val date = news.pubDate

            date?.let {
                tvTime.text = it.removeRange(11, it.length)
            }
        }

        observeLinkCreated()
    }

    private fun navigateBack() {
        requireView().findNavController().popBackStack()
    }


    private fun setUpTopAdImages(data: List<String>) {
        binding.apply {
            cvTopAds.visibility = View.VISIBLE
            val resourceAdapter = NewsResAdapter(data)
            resourceAdapter.let {
                ivTopAds.setSliderAdapter(it)
                ivTopAds.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                ivTopAds.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
                ivTopAds.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
                ivTopAds.indicatorSelectedColor = Color.WHITE
                ivTopAds.indicatorUnselectedColor = Color.GRAY
                ivTopAds.scrollTimeInSec = 2 //set scroll delay in seconds :
                ivTopAds.startAutoCycle()
            }
        }
    }

    private fun observeTopAds() {
        viewModel.topAds.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Result.Success -> {
                    it.data?.let { topAds ->
                        if (topAds.isNotEmpty()) {
                            setUpTopAdImages(topAds)
                        }
                    }
                }

                else -> {}
            }
        })
    }

    private fun observeLinkCreated() {
        viewModel.remoteLinkCreated.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Result.Loading -> {
                    showProgressBar()
                }

                is Result.Success -> {
                    hideProgressBar()
                    it.data?.let { link ->
                        shareLink(link)
                    }
                    viewModel.remoteLinkCreated.postValue(Result.NotInitialized)
                }

                is Result.Error -> {
                    hideProgressBar()
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

    private fun showProgressBar() {
        binding.progCons.visibility = View.VISIBLE
        binding.mainCons.visibility = View.INVISIBLE
    }


    private fun hideProgressBar() {
        binding.progCons.visibility = View.GONE
        binding.mainCons.visibility = View.VISIBLE
    }


    private fun getNewsAds() {

        binding.apply {
            viewModel.adsData.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is Result.Success -> {
                        it.data?.let { list ->
                            if(list.isNotEmpty()) {
                                cvAds.visibility = View.VISIBLE
                                adAdapter.differ.submitList(list)
                            }
                        }
                    }
                    else -> {
                        cvAds.visibility = View.INVISIBLE
                    }
                }
            })
        }
    }

}