package com.avicodes.halchalin.presentation.ui.home.explore

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.databinding.FragmentCommentBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.*
import com.avicodes.halchalin.data.utils.Result

class CommentFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCommentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeActivityViewModel
    private lateinit var adapter: CommentsAdapter
    val args: CommentFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val news = args.news
        viewModel = (activity as HomeActivity).viewModel

        setUpCommentsRecyclerView()
        getComments(news.newsId.toString())

        binding.apply {
            sendButton.setOnClickListener {
                val comment = etComment.editText?.text.toString()
                viewModel.postComment(
                    news.newsId.toString(),
                    comment,
                )
                etComment.editText?.text?.clear()
            }
        }

        viewModel.commentUpdated.observe(requireActivity(), Observer {
            when(it) {
                is Result.Success -> {
                    viewModel.getComments(news.newsId.toString())
                }
                else -> {}
            }
        })


        viewModel.comments.observe(requireActivity(), Observer {
            when(it) {
                is Result.Success -> {
                    adapter.differ.submitList(it.data)
                }

                else -> {}
            }
        })

        adapter.userClicked {
            navigateToUserProfile(it)
        }

    }

    private fun getComments(newsId: String) {
        viewModel.getComments(newsId = newsId)
    }

    private suspend fun getUser(userId: String): User? {
        return viewModel.getUserById(userId)
    }

    private fun setUpCommentsRecyclerView() {
        adapter = CommentsAdapter()
        binding.rvComments.adapter = adapter
        binding.rvComments.layoutManager = LinearLayoutManager(activity)
    }

    private fun navigateToUserProfile(user: User) {
        val action = CommentFragmentDirections.actionCommentFragmentToUserProfileFragment2(user)
        findNavController().navigate(action)
    }
}