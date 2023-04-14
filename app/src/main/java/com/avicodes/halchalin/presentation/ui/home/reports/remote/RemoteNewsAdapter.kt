package com.avicodes.halchalin.presentation.ui.home.reports.remote

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.databinding.ItemLoadMoreBinding
import com.avicodes.halchalin.databinding.ItemRemoteNewsBinding
import com.bumptech.glide.Glide


class RemoteNewsAdapter(
) : Adapter<RemoteNewsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemRemoteNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val data = differ.currentList[position]

                Glide.with(ivNews.context)
                    .load(data.image_url)
                    .error(R.drawable.halchal_logo_2)
                    .into(ivNews)

                data.source_id?.let { tvSource.text = it }
                data.title?.let { tvHeadline.text = it }
                val date = data.pubDate

                date?.let { date->
                    tvTime.text = date.removeRange(11, date.length)
                }

                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(data)
                    }
                }


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRemoteNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }


    private var callback = object : DiffUtil.ItemCallback<NewsRemote>() {
        override fun areItemsTheSame(oldItem: NewsRemote, newItem: NewsRemote): Boolean {
            return oldItem.link == newItem.link
        }

        override fun areContentsTheSame(oldItem: NewsRemote, newItem: NewsRemote): Boolean {
            return oldItem == newItem
        }
    }


    val differ = AsyncListDiffer(this, callback)

    private var onItemClickListener: ((NewsRemote) -> Unit)? = null

    fun setOnItemClickListener(listener: (NewsRemote) -> Unit) {
        onItemClickListener = listener
    }

}