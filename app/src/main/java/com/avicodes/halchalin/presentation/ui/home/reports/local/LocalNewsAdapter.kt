package com.avicodes.halchalin.presentation.ui.home.reports.local

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.utils.TimeCalc
import com.avicodes.halchalin.databinding.ItemLocalNewsBinding
import com.bumptech.glide.Glide


class LocalNewsAdapter: Adapter<LocalNewsAdapter.ViewHolder>(){

    inner class ViewHolder(private val binding: ItemLocalNewsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val data = differ.currentList[position]
                Glide.with(ivThumbnail.context)
                    .load(data.coverUrl)
                    .into(ivThumbnail)

                tvHeadline.text = data.newsHeadline
                tvLikes.text = data.comments.size.toString() + " Conversations"
                tvCity.text = data.location
                tvDesc.text = data.newsDesc
                tvPubDate.text = TimeCalc.getTimeAgo(data.createdAt)

                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(position)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLocalNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }


    private var callback = object : DiffUtil.ItemCallback<News> (){
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.newsId == newItem.newsId
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)


    private var onItemClickListener: ((Int)->Unit)?= null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }
}