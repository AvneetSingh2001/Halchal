package com.avicodes.halchalin.presentation.ui.home.ads

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.compose.ui.res.dimensionResource
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.models.ArticleProcessed
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.models.ads
import com.avicodes.halchalin.databinding.ItemArticleBinding
import com.avicodes.halchalin.databinding.ItemFeaturedArticleBinding
import com.avicodes.halchalin.databinding.ItemLatestNewsBinding
import com.avicodes.halchalin.databinding.ItemRemoteNewsBinding
import com.bumptech.glide.Glide

class ArticlesAdapter(
    private val onItemClickListener:(ArticleProcessed) -> Unit
) : RecyclerView.Adapter<ArticlesAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticleProcessed) {
            binding.apply {
                Glide.with(ivFeaturedArticle)
                    .load(article.articleImage)
                    .into(ivFeaturedArticle)

                tvArticleTitle.text = article.articleTitle

                tvUser.text = article.user.name

                root.setOnClickListener {
                    onItemClickListener(article)
                }


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }


    private var callback = object : DiffUtil.ItemCallback<ArticleProcessed>() {
        override fun areItemsTheSame(oldItem: ArticleProcessed, newItem: ArticleProcessed): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ArticleProcessed, newItem: ArticleProcessed): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)
}