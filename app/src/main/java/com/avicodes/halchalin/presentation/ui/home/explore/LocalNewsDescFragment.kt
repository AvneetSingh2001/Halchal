package com.avicodes.halchalin.presentation.ui.home.explore

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.data.utils.TimeCalc
import com.avicodes.halchalin.databinding.FragmentLocalNewsDescBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import com.avicodes.halchalin.presentation.ui.home.ads.AdsFragment
import com.bumptech.glide.util.Util
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView


class LocalNewsDescFragment : Fragment() {

    private var _binding: FragmentLocalNewsDescBinding? = null
    private val binding get() = _binding!!

    val args: LocalNewsDescFragmentArgs by navArgs()

    private var player: ExoPlayer? = null
    private var playbackPosition = 0L
    private var playWhenReady = true


    private lateinit var viewModel: HomeActivityViewModel
    private lateinit var adAdapter: AdsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLocalNewsDescBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as HomeActivity).viewModel
        val news = args.news

        binding.apply {
            val data = news

            tvHeadline.text = data.newsHeadline
            tvTime.text = TimeCalc.getTimeAgo(data.createdAt)
            tvLoc.text = data.location

            tvDesc.text = data.newsDesc

            adAdapter = AdsAdapter()
            binding.rvAds.adapter = adAdapter
            binding.rvAds.layoutManager = LinearLayoutManager(activity)

            data.videoUrl?.let {
                data.resUrls?.let {res ->
                    setUpBottomImages(res)
                }
                setUpVideoView(it)
            } ?: data.resUrls?.let {
                setUpHeaderImages(it)
            }

            getNewsAds()

            tvHeadline.text = data.newsHeadline

            btnComment.setOnClickListener {
                showCommentDialog(data)
            }

            btnShare.setOnClickListener {
                viewModel.createDeepLink(data)
            }


            btnBack.setOnClickListener {
                requireView().findNavController().popBackStack()
            }

            observeLinkCreated()

        }

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

    private fun setUpVideoView(videoUrl: String) {
        binding.apply {

            player = ExoPlayer.Builder(requireContext()).build()
            player?.playWhenReady = true

            player?.addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    Toast.makeText(
                        context,
                        "Can't play this video",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPlayerStateChanged(
                    playWhenReady: Boolean,
                    playbackState: Int
                ) {
                    if (playbackState == Player.STATE_BUFFERING) {
                        vvLoader.visibility = View.VISIBLE
                    } else if (playbackState == Player.STATE_READY) {
                        vvLoader.visibility = View.GONE
                    }
                }
            })

            vvNews.player = player
            vvNews.visibility = View.VISIBLE

            val mediaItem = MediaItem.fromUri(videoUrl)
            player?.setMediaItem(mediaItem)
            player?.seekTo(playbackPosition)
            player?.playWhenReady = playWhenReady
            player?.prepare()
        }
    }

    private fun releasePlayer() {
        player?.let {
            playbackPosition = it.currentPosition
            playWhenReady = it.playWhenReady
            it.release()
            player = null
        }
    }

    override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }


    private fun setUpHeaderImages(data: List<String>) {
        binding.apply {
            ivNews.visibility = View.VISIBLE
            val resourceAdapter = NewsResAdapter(data)
            resourceAdapter.let {
                ivNews.setSliderAdapter(it)
                ivNews.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                ivNews.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
                ivNews.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
                ivNews.indicatorSelectedColor = Color.WHITE
                ivNews.indicatorUnselectedColor = Color.GRAY
                ivNews.scrollTimeInSec = 4 //set scroll delay in seconds :
                ivNews.startAutoCycle()
            }
        }
    }

    private fun setUpBottomImages(data: List<String>) {
        binding.apply {
            cvImagesBottom.visibility = View.VISIBLE
            val resourceAdapter = NewsResAdapter(data)
            resourceAdapter.let {
                ivNewsBottom.setSliderAdapter(it)
                ivNewsBottom.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                ivNewsBottom.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
                ivNewsBottom.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
                ivNewsBottom.indicatorSelectedColor = Color.WHITE
                ivNewsBottom.indicatorUnselectedColor = Color.GRAY
                ivNewsBottom.scrollTimeInSec = 4 //set scroll delay in seconds :
                ivNewsBottom.startAutoCycle()
            }
        }
    }

    private fun showCommentDialog(news: News) {
        val action =
            LocalNewsDescFragmentDirections.actionLocalNewsDescFragmentToCommentFragment(news)
        requireView().findNavController().navigate(action)
    }


    private fun observeLinkCreated() {
        viewModel.linkCreated.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Result.Loading -> {
                    binding.animationView.progress = 0f
                    binding.btnShare.visibility = View.INVISIBLE
                    binding.animationView.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.btnShare.visibility = View.VISIBLE
                    binding.animationView.visibility = View.GONE
                    it.data?.let { link ->
                        shareLink(link)
                    }
                    viewModel.linkCreated.postValue(Result.NotInitialized)
                }
                is Result.Error -> {
                    binding.btnShare.visibility = View.VISIBLE
                    binding.animationView.visibility = View.GONE
                    Toast.makeText(requireContext(), "Error sharing news", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    binding.btnShare.visibility = View.VISIBLE
                    binding.animationView.visibility = View.GONE
                }
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


}