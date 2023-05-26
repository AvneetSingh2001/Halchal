package com.avicodes.halchalin.presentation.ui.home.ads

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.avicodes.halchalin.data.models.ArticleProcessed
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.databinding.FragmentAllArticlesBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AllArticlesFragment : Fragment(), FeaturedArticleAdapter.FeaturedOnClickListener {

    private var _binding: FragmentAllArticlesBinding? = null
    private val binding get() = _binding!!

    private lateinit var featuredArticleAdapter: FeaturedArticleAdapter

    private lateinit var viewModel: HomeActivityViewModel

    val args: AllArticlesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAllArticlesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as HomeActivity).viewModel

        val userId = args.userId

        val isUserArticles = userId != null

        featuredArticleAdapter = FeaturedArticleAdapter( isUserArticles, this)

        binding.rvNationalNews.adapter = featuredArticleAdapter
        binding.rvNationalNews.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        if (userId != null) {
            userId.let {
                getUserArticles(userId)
            }
        } else {
            getAllArticles()
        }

    }


    private fun getAllArticles() {
        viewModel.allArticles.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Result.Error -> {
                    Toast.makeText(context, "An Error Occurred", Toast.LENGTH_LONG).show()
                    Log.e("Avneet Error", response.exception?.message.toString())
                }

                is Result.Success -> {
                    response.data?.let {
                        featuredArticleAdapter.differ.submitList(it)
                    }
                }

                else -> {
                }
            }
        })
    }

    private fun getUserArticles(userId: String) {
        viewModel.getUserArticles(userId)
        viewModel.userArticles.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Result.Error -> {
                    hideProgressBar()
                    Toast.makeText(context, "An Error Occurred", Toast.LENGTH_LONG).show()
                    Log.e("Avneet Error", response.exception?.message.toString())
                }

                is Result.Success -> {
                    binding.rvNationalNews.smoothScrollToPosition(0)
                    response.data?.let {
                        featuredArticleAdapter.differ.submitList(it)
                    }
                    hideProgressBar()
                }

                is Result.Loading -> {
                    showProgressBar()
                }

                else -> {
                }
            }
        })
    }

    private fun showProgressBar() {
        binding.progCons.visibility = View.VISIBLE
        binding.mainCons.visibility = View.INVISIBLE
    }


    private fun hideProgressBar() {
        binding.progCons.visibility = View.GONE
        binding.mainCons.visibility = View.VISIBLE
    }

    override fun onItemClickListener(article: ArticleProcessed) {
        val action =
            AllArticlesFragmentDirections.actionAllArticlesFragmentToArticleDetailFragment(article)
        requireView().findNavController().navigate(action)
    }

    private fun popDeleteDialog(article: ArticleProcessed) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle("Delete Article")
                .setMessage("Are you sure you want to delete this article")
                .setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton("Yes") { dialog, which ->
                    deleteArticle(article, dialog)
                }
                .show()
        }
    }

    private fun deleteArticle(article: ArticleProcessed, dialog: DialogInterface, ) {
        viewModel.curUser.observe(viewLifecycleOwner, Observer {user ->
            if(user?.userId == article.user.userId) {
                lifecycleScope.launch {
                    viewModel.deleteArticle(articleId = article.articleId).collectLatest {
                        when(it) {
                            is Result.Success -> {
                                hideProgressBar()
                                getUserArticles(user.userId)
                            }

                            is Result.Loading -> {
                                showProgressBar()
                            }

                            is Result.Error ->  {
                                hideProgressBar()
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                            }

                            else -> {}
                        }
                    }
                }
            }
        })
    }

    override fun onDeleteClickListener(article: ArticleProcessed) {
        popDeleteDialog(article)
    }

}