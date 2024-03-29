package com.avicodes.halchalin.presentation.ui.home.localnews

import android.animation.Animator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.utils.TimeCalc
import com.avicodes.halchalin.databinding.ItemLocalNewsBinding
import com.bumptech.glide.Glide


class LocalNewsAdapter : Adapter<LocalNewsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemLocalNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val data = differ.currentList[position]

                data.coverUrl?.let {
                    Glide.with(ivThumbnail.context)
                        .load(it)
                        .into(ivThumbnail)
                }

                tvHeadline.text = data.newsHeadline
                // tvCity.text = data.location
                tvDesc.text = data.newsDesc
                tvTime.text = TimeCalc.getTimeAgo(data.createdAt)
                tvCity.text = data.location
                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(data)
                    }
                }

                btnComment.setOnClickListener {
                    commentClickListener?.let {
                        it(data)
                    }
                }

                btnShare.setOnClickListener {
                    shareClickListener?.let {
                        animationView.progress = 0f
                        btnShare.visibility = View.INVISIBLE
                        animationView.visibility = View.VISIBLE
                        animationView.addAnimatorListener(object : Animator.AnimatorListener {
                            override fun onAnimationStart(p0: Animator) {

                            }

                            override fun onAnimationEnd(p0: Animator) {
                                btnShare.visibility = View.VISIBLE
                                animationView.visibility = View.GONE
                            }

                            override fun onAnimationCancel(p0: Animator) {
                            }

                            override fun onAnimationRepeat(p0: Animator) {
                                btnShare.visibility = View.VISIBLE
                                animationView.visibility = View.GONE
                            }

                        })
                        it(data)
                    }
                }


            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemLocalNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position >= 0)
            holder.bind(position)
    }


    private var callback = object : DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)


    private var onItemClickListener: ((News) -> Unit)? = null

    fun setOnItemClickListener(listener: (News) -> Unit) {
        onItemClickListener = listener
    }


    private var commentClickListener: ((News) -> Unit)? = null
    fun setOnCommentClickListener(listener: (News) -> Unit) {
        commentClickListener = listener
    }

    private var shareClickListener: ((News) -> Unit)? = null
    fun setOnShareClickListener(listener: (News) -> Unit) {
        shareClickListener = listener
    }
}