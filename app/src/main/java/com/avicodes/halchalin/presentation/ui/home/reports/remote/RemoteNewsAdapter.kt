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

    val NEWS_ITEM_BINDING = 0
    val LOAD_ITEM_BINDING = 1

    private var newsBinding: ItemRemoteNewsBinding? = null
    private var loadMoreBinding: ItemLoadMoreBinding? = null

    inner class ViewHolder : RecyclerView.ViewHolder {

        constructor(binding: ItemRemoteNewsBinding): super(binding.root) {
            newsBinding = binding
        }

        constructor(binding: ItemLoadMoreBinding): super(binding.root) {
            loadMoreBinding = binding
        }

        fun bind(position: Int) {

            if (position >= 1) {
                newsBinding?.run {
                    val data = differ.currentList[position]

                    Glide.with(ivNews.context)
                        .load(data.image_url)
                        .error(R.drawable.halchal_logo_2)
                        .into(ivNews)

                    data.source_id?.let { tvSource.text = it }
                    data.title?.let { tvHeadline.text = it }
                    val date = data.pubDate

                    date?.let { date ->
                        tvTime.text = date.removeRange(11, date.length)
                    }

                    root.setOnClickListener {
                        onItemClickListener?.let {
                            it(data)
                        }
                    }
                }

                loadMoreBinding?.run {
                    tvLoad.setOnClickListener {
                        loadMoreClickListener
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return when (viewType) {
            NEWS_ITEM_BINDING -> {
                ViewHolder(
                    ItemRemoteNewsBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            LOAD_ITEM_BINDING -> {
                ViewHolder(
                    ItemLoadMoreBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                ViewHolder(
                    ItemRemoteNewsBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == differ.currentList.size - 1)
            LOAD_ITEM_BINDING
        else
            NEWS_ITEM_BINDING
    }


    override fun getItemCount(): Int {
        return differ.currentList.size + 1
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

    private var loadMoreClickListener: ((Unit) -> Unit)? = null

    fun loadMoreClickListener(listener: (Unit) -> Unit) {
        loadMoreClickListener = listener
    }

}