package com.avicodes.halchalin.presentation.ui.home.ads

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.data.utils.TimeCalc
import com.avicodes.halchalin.databinding.FragmentArticleDetailBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import com.avicodes.halchalin.presentation.ui.home.reports.remote.DetailedRemoteFragmentArgs
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch


class ArticleDetailFragment : Fragment() {

    private var _binding: FragmentArticleDetailBinding? = null
    private val binding get() = _binding!!

    val args: ArticleDetailFragmentArgs by navArgs()
    private lateinit var viewModel: HomeActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentArticleDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as HomeActivity).viewModel
        val article = args.article

        binding.apply {
            Glide.with(ivArticle.context)
                .load(article.articleImage)
                .into(ivArticle)

            tvArticleDesc.text = article.articleDesc
            tvArticleTitle.text = article.articleTitle
            if (article.articleTag.equals("null")) {
                cvTag.visibility = View.GONE
            } else {
                tvTag.text = article.articleTag
            }

            tvUserBio.text = article.user.about

            Glide.with(ivUser.context)
                .load(article.user.imgUrl)
                .circleCrop()
                .into(ivUser)

            tvUserName.text = article.user.name

            tvDate.text = TimeCalc.getTimeAgo(article.date.toLong())



            btnShare.setOnClickListener {
                viewModel.createArticleDeepLink(article = article)
            }

            clUser.setOnClickListener {
                navigateToUserProfile(article.user)
            }

            ivUser.setOnClickListener {
                navigateToUserProfile(article.user)
            }
        }

        observeLinkCreated()
    }

    private fun navigateToUserProfile(user: User) {
        val action = ArticleDetailFragmentDirections.actionArticleDetailFragmentToUserProfileFragment(user)
        requireView().findNavController().navigate(action)
    }


    private fun observeLinkCreated() {
        viewModel.articleLinkCreated.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Result.Loading -> {
                    binding.animationView.visibility = View.VISIBLE
                    binding.animationView.progress = 0f
                    binding.icShare.visibility = View.GONE
                }
                is Result.Success -> {
                    it.data?.let { link ->
                        shareLink(link)
                    }
                    binding.animationView.visibility = View.GONE
                    binding.icShare.visibility = View.VISIBLE
                    viewModel.articleLinkCreated.postValue(Result.NotInitialized)
                }
                is Result.Error -> {
                    binding.animationView.visibility = View.GONE
                    binding.icShare.visibility = View.VISIBLE
                    Toast.makeText(requireContext(), "Error sharing news", Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    binding.animationView.visibility = View.GONE
                    binding.icShare.visibility = View.VISIBLE
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


}