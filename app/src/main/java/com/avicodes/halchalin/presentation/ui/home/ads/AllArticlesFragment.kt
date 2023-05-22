package com.avicodes.halchalin.presentation.ui.home.ads

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
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.databinding.FragmentAllArticlesBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel


class AllArticlesFragment : Fragment() {

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

        featuredArticleAdapter = FeaturedArticleAdapter {
            val action =
                AllArticlesFragmentDirections.actionAllArticlesFragmentToArticleDetailFragment(it)
            requireView().findNavController().navigate(action)
        }

        binding.rvNationalNews.adapter = featuredArticleAdapter
        binding.rvNationalNews.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        if (userId != null) {
            userId?.let {
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
                    binding.rvNationalNews.smoothScrollToPosition(0)
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
                    Toast.makeText(context, "An Error Occurred", Toast.LENGTH_LONG).show()
                    Log.e("Avneet Error", response.exception?.message.toString())
                }

                is Result.Success -> {
                    binding.rvNationalNews.smoothScrollToPosition(0)
                    response.data?.let {
                        featuredArticleAdapter.differ.submitList(it)
                    }
                }

                else -> {
                }
            }
        })
    }


}