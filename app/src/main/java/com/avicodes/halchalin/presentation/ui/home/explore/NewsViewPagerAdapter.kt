package com.avicodes.halchalin.presentation.ui.home.explore


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.utils.TimeCalc
import com.avicodes.halchalin.databinding.ItemNewsDescBinding
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView


class NewsViewPagerAdapter: Adapter<NewsViewPagerAdapter.ViewHolder>(){

    inner class ViewHolder(private val binding: ItemNewsDescBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val data = differ.currentList[position]

                tvHeadline.text = data.newsHeadline
                tvTime.text = TimeCalc.getTimeAgo(data.createdAt)
                tvLikes.text = data.likes.size.toString()
                tvCity.text = data.location

                val resourceAdapter = data.resUrls?.let { NewsResAdapter(it) }
                resourceAdapter?.let {
                    ivNews.setSliderAdapter(it)
                    ivNews.setIndicatorAnimation(IndicatorAnimationType.WORM) //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                    ivNews.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
                    ivNews.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
                    ivNews.indicatorSelectedColor = Color.WHITE
                    ivNews.indicatorUnselectedColor = Color.GRAY
                    ivNews.scrollTimeInSec = 4 //set scroll delay in seconds :
                    ivNews.startAutoCycle()
                }
                tvHeadline.text = data.newsHeadline

                cvLikes.setOnClickListener {
                    likeClickListener?.let {
                        it(position)
                    }
                }

                cvComment.setOnClickListener {
                    commentClickListener?.let {
                        it(position)
                    }
                }

                cvShare.setOnClickListener {
                    shareClickListener?.let {
                        it(position)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsDescBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }


    private var callback = object : DiffUtil.ItemCallback<News> (){
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.newsId == newItem.newsId
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)

    private var likeClickListener: ((Int)->Unit)?= null
    fun setOnLikeClickListener(listener: (Int) -> Unit) {
        likeClickListener = listener
    }

    private var commentClickListener: ((Int)->Unit)?= null
    fun setOnCommentClickListener(listener: (Int) -> Unit) {
        commentClickListener = listener
    }

    private var shareClickListener: ((Int)->Unit)?= null
    fun setOnShareClickListener(listener: (Int) -> Unit) {
        shareClickListener = listener
    }

}