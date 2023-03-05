package com.avicodes.halchalin.presentation.ui.home.explore

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.models.Comment
import com.avicodes.halchalin.data.models.CommentProcessed
import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.data.utils.TimeCalc
import com.avicodes.halchalin.databinding.ItemCommentBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
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
}
