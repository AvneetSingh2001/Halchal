package com.avicodes.halchalin.presentation.ui.home.reports.remote

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.databinding.FragmentDetailedRemoteBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import com.bumptech.glide.Glide


class DetailedRemoteFragment : Fragment() {
    private var _binding: FragmentDetailedRemoteBinding? = null
    private val binding get() = _binding!!

    val args: DetailedRemoteFragmentArgs by navArgs()
    private lateinit var viewModel: HomeActivityViewModel

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

            if(news.description != null) {
                tvDesc.text = news.description
            } else if(news.content != null){
                tvDesc.text = news.content
            } else {
                tvDesc.text = news.title
            }


            Glide.with(ivNews.context)
                .load(news.image_url)
                .error(R.drawable.halchal_logo_2)
                .into(ivNews)

            tvSource.text = news.source_id

            val date = news.pubDate

            date?.let {
                tvTime.text = it.removeRange(11, it.length)
            }


            btnBack.setOnClickListener {
                navigateBack()
            }

            btnShare.setOnClickListener {
                //viewModel.createRemoteDeepLink(news)
            }

        }

        observeLinkCreated()
    }

    private fun navigateBack() {
        requireView().findNavController().popBackStack()
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

}