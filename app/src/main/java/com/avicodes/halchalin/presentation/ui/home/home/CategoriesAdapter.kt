package com.avicodes.halchalin.presentation.ui.home.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.models.Categories
import com.avicodes.halchalin.data.models.LatestNews
import com.avicodes.halchalin.databinding.ItemCategoriesBinding
import com.avicodes.halchalin.databinding.ItemLatestNewsBinding
import com.bumptech.glide.Glide

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemCategoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val data = differ.currentList[position]
                data?.let {
                    Glide.with(ivCategory.context)
                        .load(data.imgUrl)
                        .circleCrop()
                        .error(R.drawable.world)
                        .into(ivCategory)

                    tvName.text = data.name

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
            ItemCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }


    private var callback = object : DiffUtil.ItemCallback<Categories>() {
        override fun areItemsTheSame(oldItem: Categories, newItem: Categories): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Categories, newItem: Categories): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)

    private var onItemClickListener: ((Categories) -> Unit)? = null

    fun setOnItemClickListener(listener: (Categories) -> Unit) {
        onItemClickListener = listener
    }
}