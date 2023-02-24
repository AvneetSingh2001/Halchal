package com.avicodes.halchalin.presentation.ui.home.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.databinding.ItemLatestNewsBinding
import com.avicodes.halchalin.databinding.ItemRemoteNewsBinding
import com.avicodes.halchalin.presentation.ui.home.reports.remote.RemoteNewsAdapter
import com.bumptech.glide.Glide

class LatestNewsAdapter: RecyclerView.Adapter<LatestNewsAdapter.ViewHolder>(){

    inner class ViewHolder(private val binding: ItemLatestNewsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val data = differ.currentList[position]
                data?.let { tvHeadline.text = data }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLatestNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }


    private var callback = object : DiffUtil.ItemCallback<String> (){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }


    val differ = AsyncListDiffer(this, callback)
}