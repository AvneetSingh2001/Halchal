package com.avicodes.halchalin.presentation.ui.home.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.utils.TimeCalc
import com.avicodes.halchalin.databinding.ItemLatestNewsBinding
import com.bumptech.glide.Glide

class LatestNewsAdapter : RecyclerView.Adapter<LatestNewsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemLatestNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val data = differ.currentList[position]
                data?.let {
                    tvHeadline.text = data.newsHeadline


                    val loc = data.location?.split(", ")?.toTypedArray()
                    loc?.let {
                        if (loc.isNotEmpty()) {
                            tvType.text = loc[0]
                        }
                    }

                    tvTime.text = TimeCalc.getTimeAgo(data.createdAt)


                    Glide.with(ivNews.context)
                        .load(data.coverUrl)
                        .error(R.drawable.halchal_logo_2)
                        .into(ivNews)

                    root.setOnClickListener {
                        onItemClickListener?.let {
                            it(data)
                        }
                    }

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemLatestNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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
}