package com.avicodes.halchalin.presentation.ui.home.ads

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.avicodes.halchalin.data.models.ArticleProcessed
import com.avicodes.halchalin.databinding.ItemArticleBinding
import com.avicodes.halchalin.databinding.ItemFeaturedArticleBinding
import com.bumptech.glide.Glide

class FeaturedArticleAdapter(
    private val isUserArticles: Boolean,
    private val featuredOnClickListener: FeaturedOnClickListener
) : RecyclerView.Adapter<FeaturedArticleAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemFeaturedArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticleProcessed) {
            binding.apply {
                Glide.with(ivFeaturedArticle)
                    .load(article.articleImage)
                    .into(ivFeaturedArticle)

                tvArticleTitle.text = article.articleTitle

                tvUserName.text = article.user.name
                tvUserBio.text = article.user.about

                Glide.with(ivUser.context)
                    .load(article.user.imgUrl)
                    .circleCrop()
                    .into(ivUser)

                root.setOnClickListener {
                    featuredOnClickListener.onItemClickListener(article)
                }

                if(isUserArticles) {
                    clDelete.visibility = View.VISIBLE
                    clUserDetails.visibility = View.GONE
                }

                ivDelete.setOnClickListener {
                    featuredOnClickListener.onDeleteClickListener(article)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemFeaturedArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }


    private var callback = object : DiffUtil.ItemCallback<ArticleProcessed>() {
        override fun areItemsTheSame(
            oldItem: ArticleProcessed,
            newItem: ArticleProcessed
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ArticleProcessed,
            newItem: ArticleProcessed
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)


    interface FeaturedOnClickListener {
        fun onItemClickListener(article: ArticleProcessed)
        fun onDeleteClickListener(article: ArticleProcessed)
    }
}
