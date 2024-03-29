package com.avicodes.halchalin.presentation.ui.home.localnews.localvp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.avicodes.halchalin.data.models.ExoPlayerItem
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.databinding.FragmentNewsVpBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NewsVpFragment : Fragment() {

    private var _binding: FragmentNewsVpBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: NewsViewPagerAdapter
    private lateinit var viewModel: HomeActivityViewModel

    private var newsList: List<News> = mutableListOf()
    private var sharedNews: List<News> = mutableListOf()

    private val exoPlayerItems = ArrayList<ExoPlayerItem>()

   // val args: NewsVpFragmentArgs by navArgs()

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


        binding.videoViewPager.setPageTransformer(DepthPageTransformer())


        adapter.setOnCommentClickListener {
           showCommentDialog(it.newsId)
        }

        adapter.setOnShareClickListener {
            viewModel.createDeepLink(it)
        }

        adapter.setOnSeeMoreClickListener {
            val action = NewsVpFragmentDirections.actionNewsVpFragmentToLocalNewsDescFragment(it)
            requireView().findNavController().navigate(action)
        }

        binding.apply {
            btnBack.setOnClickListener {
                requireView().findNavController().popBackStack()
            }
        }

        observeLinkCreated()
    }

    private fun observeLinkCreated() {
        viewModel.linkCreated.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Result.Loading -> {
                    showProgressBar()
                }
                is Result.Success -> {
                    hideProgressBar()
                    it.data?.let { link ->
                        shareLink(link)
                    }
                    viewModel.linkCreated.postValue(Result.NotInitialized)
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


    private fun showCommentDialog(newsId: String) {
        val action = NewsVpFragmentDirections.actionNewsVpFragmentToCommentFragment(newsId)
        requireView().findNavController().navigate(action)
    }

    private fun showProgressBar() {
        binding.progCons.visibility = View.VISIBLE
        binding.mainCons.visibility = View.INVISIBLE
    }


    private fun hideProgressBar() {
        binding.progCons.visibility = View.GONE
        binding.mainCons.visibility = View.VISIBLE
    }

    private fun setUpLocalNewsRecyclerView() {
        binding.apply {
            adapter = NewsViewPagerAdapter(
                requireContext(),
                object : NewsViewPagerAdapter.OnVideoPreparedListener {
                    override fun onVideoPrepared(exoPlayerItem: ExoPlayerItem) {
                        exoPlayerItems.add(exoPlayerItem)
                    }
                })
            videoViewPager.adapter = adapter

            videoViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    val previousIndex = exoPlayerItems.indexOfFirst { it.exoPlayer.isPlaying }
                    if (previousIndex != -1) {
                        val player = exoPlayerItems[previousIndex].exoPlayer
                        player.pause()
                        player.playWhenReady = false
                    }
                    val newIndex = exoPlayerItems.indexOfFirst { it.position == position }
                    if (newIndex != -1) {
                        val player = exoPlayerItems[newIndex].exoPlayer
                        player.playWhenReady = true
                        player.play()
                    }
                }
            })
        }
    }

    override fun onPause() {
        super.onPause()
        val index = exoPlayerItems.indexOfFirst { it.position == binding.videoViewPager.currentItem }
        if (index != -1) {
            val player = exoPlayerItems[index].exoPlayer
            player.pause()
            player.playWhenReady = false
        }
    }

    override fun onResume() {
        super.onResume()

        val index = exoPlayerItems.indexOfFirst { it.position == binding.videoViewPager.currentItem }
        if (index != -1) {
            val player = exoPlayerItems[index].exoPlayer
            player.playWhenReady = true
            player.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (exoPlayerItems.isNotEmpty()) {
            for (item in exoPlayerItems) {
                val player = item.exoPlayer
                player.stop()
                player.clearMediaItems()
            }
        }
    }
    class DepthPageTransformer : ViewPager2.PageTransformer {

        private val MIN_SCALE = 0.75F

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

