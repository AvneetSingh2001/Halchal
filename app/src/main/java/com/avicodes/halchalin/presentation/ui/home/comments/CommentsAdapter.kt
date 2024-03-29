package com.avicodes.halchalin.presentation.ui.home.comments

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.models.CommentProcessed
import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.data.utils.TimeCalc
import com.avicodes.halchalin.databinding.ItemCommentBinding
import com.bumptech.glide.Glide

class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(commentData: CommentProcessed) {
            Log.e("Comment", commentData.toString())
            binding.apply {
                tvComment.text = commentData.comment

                val user = commentData.user

                Glide.with(ivUser)
                    .load(user.imgUrl)
                    .error(R.drawable.baseline_person_24)
                    .circleCrop()
                    .into(ivUser)


                tvName.text = user.name
                tvTime.text = TimeCalc.getTimeAgo(commentData.time)

                tvName.setOnClickListener {
                    userClicked?.let {
                        it(user)
                    }
                }

                ivUser.setOnClickListener {
                    userClicked?.let {
                        it(user)
                    }
                }

                val isCurUserComment = checkCurUser?.let {
                    it(user.userId)
                }

                isCurUserComment?.let {
                    if(isCurUserComment) {
                        tvDelete.visibility = View.VISIBLE
                    }
                }

                tvDelete.setOnClickListener {
                    deleteClicked?.let {
                        it(commentData.commentId)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int) = position

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    private var callback = object : DiffUtil.ItemCallback<CommentProcessed>() {
        override fun areItemsTheSame(oldItem: CommentProcessed, newItem: CommentProcessed): Boolean {
            return oldItem.time == newItem.time
        }

        override fun areContentsTheSame(oldItem: CommentProcessed, newItem: CommentProcessed): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)

    private var getUserById: ((String) -> User?)? = null

    fun getUserByIdFun(listener: (String) -> User?) {
        getUserById = listener
    }


    private var checkCurUser: ((String) -> Boolean)? = null

    fun checkCurUser(listener: (String) -> Boolean) {
        checkCurUser = listener
    }


    private var deleteClicked: ((String) -> Unit)?= null

    fun deleteClicked(listener: (String) -> Unit) {
        deleteClicked = listener
    }


    private var userClicked: ((User) -> Unit)?= null

    fun userClicked(listener: (User) -> Unit) {
        userClicked = listener
    }
}
