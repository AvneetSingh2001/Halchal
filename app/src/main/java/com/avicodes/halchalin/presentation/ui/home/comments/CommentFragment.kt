package com.avicodes.halchalin.presentation.ui.home.comments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.databinding.FragmentCommentBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.avicodes.halchalin.data.utils.Result
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CommentFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCommentBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<HomeActivityViewModel>()

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

        val itemId = args.itemId
        setUpCommentsRecyclerView()
        getComments(itemId)

        binding.apply {
            sendButton.setOnClickListener {
                val comment = etComment.editText?.text.toString()

                viewModel.curUser.observe(viewLifecycleOwner, Observer { user ->
                    user?.let {
                        lifecycleScope.launch {
                            viewModel.postComment(
                                itemId,
                                comment,
                                user.userId
                            ).collectLatest {
                                when (it) {
                                    is Result.Success -> {
                                        getComments(itemId)
                                    }

                                    is Result.Loading -> {
                                        showProg()
                                    }

                                    is Result.Error -> {
                                        hideProg()
                                        Toast.makeText(
                                            context,
                                            "Something went wrong",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    else -> {}
                                }
                            }
                        }
                    }
                })

                etComment.editText?.text?.clear()
            }
        }



        viewModel.comments.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Result.Success -> {
                    hideProg()
                    adapter.differ.submitList(it.data)
                    viewModel.comments.postValue(Result.NotInitialized)
                }

                is Result.Loading -> {
                    showProg()
                }

                is Result.Error -> {
                    hideProg()
                    Log.e("Comment Error", it.exception.toString())
                }

                else -> {

                }
            }
        })

        adapter.userClicked {
            navigateToUserProfile(it)
        }

        adapter.checkCurUser {
            viewModel.curUser.value?.userId == it
        }

        adapter.deleteClicked { commendId ->
            popDeleteDialog(commendId)
        }
    }


    private fun popDeleteDialog(commentId: String) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle("Delete Comment")
                .setMessage("Are you sure you want to delete this comment")
                .setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton("Yes") { dialog, which ->
                    deleteComment(commentId, dialog)
                }
                .show()
        }
    }

    fun showProg() {
        binding.mainCons.visibility = View.INVISIBLE
        binding.progBar.visibility = View.VISIBLE
    }

    fun hideProg() {
        binding.mainCons.visibility = View.VISIBLE
        binding.progBar.visibility = View.GONE
    }

    private fun deleteComment(commentId: String, dialog: DialogInterface) {
        lifecycleScope.launch {
            viewModel.deleteComment(commentId).collectLatest {
                when (it) {
                    is Result.Success -> {
                        hideProg()
                        getComments(args.itemId)
                    }

                    is Result.Loading -> {
                        showProg()
                    }

                    is Result.Error -> {
                        hideProg()
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                            .show()
                    }

                    else -> {}

                }
            }
        }
    }

    private fun getComments(newsId: String) {
        viewModel.getComments(newsId = newsId)
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