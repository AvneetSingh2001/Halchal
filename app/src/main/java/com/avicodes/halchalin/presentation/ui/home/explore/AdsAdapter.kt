package com.avicodes.halchalin.presentation.ui.home.explore

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.avicodes.halchalin.data.models.ads
import com.avicodes.halchalin.databinding.ItemAdBinding
import com.bumptech.glide.Glide

class AdsAdapter : RecyclerView.Adapter<AdsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemAdBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(adData: ads) {
            binding.apply {

                Glide.with(ivAd.context)
                    .load(adData.imgUrl)
                    .into(ivAd)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    private var callback = object : DiffUtil.ItemCallback<ads>() {
        override fun areItemsTheSame(oldItem: ads, newItem: ads): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ads, newItem: ads): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)


}