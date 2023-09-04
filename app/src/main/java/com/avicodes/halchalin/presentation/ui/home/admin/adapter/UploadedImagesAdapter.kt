package com.avicodes.halchalin.presentation.ui.home.admin.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.avicodes.halchalin.databinding.ItemImagesBinding
import com.bumptech.glide.Glide

class UploadedImagesAdapter: RecyclerView.Adapter<UploadedImagesAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemImagesBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val data = differ.currentList[position]
                data?.let {
                    Glide.with(ivNews.context)
                        .load(data)
                        .into(ivNews)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemImagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }


    private var callback = object : DiffUtil.ItemCallback<Uri> (){
        override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean {
            return oldItem == newItem
        }
    }


    val differ = AsyncListDiffer(this, callback)
}