package com.avicodes.halchalin.presentation.ui.home.reports.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.databinding.ItemRemoteNewsBinding
import com.bumptech.glide.Glide

class CategoryNewsAdapter(
): PagingDataAdapter<NewsRemote, CategoryNewsAdapter.ViewHolder>(DiffUtilCallBack) {

    inner class ViewHolder(private val binding: ItemRemoteNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: NewsRemote) {
            binding.apply {

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
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRemoteNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }


    private var onItemClickListener: ((NewsRemote) -> Unit)? = null

    fun setOnItemClickListener(listener: (NewsRemote) -> Unit) {
        onItemClickListener = listener
    }

    object DiffUtilCallBack : DiffUtil.ItemCallback<NewsRemote>() {
        override fun areItemsTheSame(oldItem: NewsRemote, newItem: NewsRemote): Boolean {
            return oldItem.link == newItem.link
        }

        override fun areContentsTheSame(oldItem: NewsRemote, newItem: NewsRemote): Boolean {
            return oldItem == newItem
        }
    }

}




