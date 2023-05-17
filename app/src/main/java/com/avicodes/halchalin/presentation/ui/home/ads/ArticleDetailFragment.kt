package com.avicodes.halchalin.presentation.ui.home.ads

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.avicodes.halchalin.R
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

            Glide.with(ivUser.context)
                .load(article.user.imgUrl)
                .into(ivUser)

            tvUserName.text = article.user.name

            tvDate.text = TimeCalc.getTimeAgo(article.date.toLong())

            btnBack.setOnClickListener {
                requireView().findNavController().popBackStack()
            }
        }
    }

}