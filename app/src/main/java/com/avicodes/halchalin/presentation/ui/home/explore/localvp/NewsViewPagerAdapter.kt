package com.avicodes.halchalin.presentation.ui.home.explore.localvp


import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.avicodes.halchalin.data.models.ExoPlayerItem
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.utils.TimeCalc
import com.avicodes.halchalin.databinding.ItemNewsDescBinding
import com.avicodes.halchalin.presentation.ui.home.explore.NewsResAdapter
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView


class NewsViewPagerAdapter(
    var context: Context,
    var videoPreparedListener: OnVideoPreparedListener
) : Adapter<NewsViewPagerAdapter.ViewHolder>() {



    private lateinit var exoPlayer: ExoPlayer
    private lateinit var mediaSource: MediaSource


    inner class ViewHolder(private val binding: ItemNewsDescBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {

            binding.apply {
                val data = differ.currentList[position]

                tvHeadline.text = data.newsHeadline
                tvTime.text = TimeCalc.getTimeAgo(data.createdAt)
                tvLoc.text = data.location
                val desc = data.newsDesc
                val stIdx = 0
                var enIdx = desc?.length ?: 0

                var displayDesc = desc?.substring(stIdx, enIdx)

                if (enIdx > 200) {
                    enIdx = 200
                    displayDesc = desc?.substring(stIdx, enIdx).plus("...")
                    tvSeeMore.visibility = View.VISIBLE
                }

                tvDesc.text = displayDesc

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

                btnComment.setOnClickListener {
                    commentClickListener?.let {
                        it(data)
                    }
                }

                btnShare.setOnClickListener {
                    shareClickListener?.let {
                        it(data)
                    }
                }

                tvSeeMore.setOnClickListener {
                    seeMoreClickListener?.let {
                        it(data)
                    }
                }


//                rgContent.setOnCheckedChangeListener { group, checkedId ->
//                    if (checkedId == R.id.enLang) {
//                        exoPlayer.pause()
//                        exoPlayer.playWhenReady = false
//
//                        vvNews.visibility = View.GONE
//                        ivNews.visibility = View.VISIBLE
//                        enLang.setTextColor(
//                            ContextCompat.getColor(context, R.color.white)
//                        )
//                        hiLang.setTextColor(
//                            ContextCompat.getColor(context, R.color.black)
//                        )
//
//                    }
//                    if (checkedId == R.id.hiLang) {
//                        hiLang.setTextColor(
//                            ContextCompat.getColor(context, R.color.white)
//                        )
//                        enLang.setTextColor(
//                            ContextCompat.getColor(context, R.color.black)
//                        )
//
//
//                        vvNews.visibility = View.VISIBLE
//                        ivNews.visibility = View.INVISIBLE
//                        data.videoUrl?.let {
//                            exoPlayer = ExoPlayer.Builder(context).build()
//                            exoPlayer.addListener(object : Player.Listener {
//                                override fun onPlayerError(error: PlaybackException) {
//                                    super.onPlayerError(error)
//                                    Toast.makeText(
//                                        context,
//                                        "Can't play this video",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                }
//
//                                override fun onPlayerStateChanged(
//                                    playWhenReady: Boolean,
//                                    playbackState: Int
//                                ) {
//                                    if (playbackState == Player.STATE_BUFFERING) {
//                                        vvLoader.visibility = View.VISIBLE
//                                    } else if (playbackState == Player.STATE_READY) {
//                                        vvLoader.visibility = View.GONE
//                                    }
//                                }
//                            })
//
//                            vvNews.player = exoPlayer
//
//                            exoPlayer.seekTo(0)
//                            exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
//
//                            val dataSourceFactory = DefaultDataSource.Factory(context)
//
//                            mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
//                                .createMediaSource(MediaItem.fromUri(Uri.parse(it)))
//
//                            exoPlayer.setMediaSource(mediaSource)
//                            exoPlayer.prepare()
//
//                            if (absoluteAdapterPosition == 0) {
//                                exoPlayer.playWhenReady = true
//                                exoPlayer.play()
//                            }
//
//                            videoPreparedListener.onVideoPrepared(
//                                ExoPlayerItem(
//                                    exoPlayer,
//                                    absoluteAdapterPosition
//                                )
//                            )
//                        }
//                    }
//                }


            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemNewsDescBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            return oldItem.newsId == newItem.newsId
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)

    private var commentClickListener: ((News) -> Unit)? = null
    fun setOnCommentClickListener(listener: (News) -> Unit) {
        commentClickListener = listener
    }

    private var shareClickListener: ((News) -> Unit)? = null
    fun setOnShareClickListener(listener: (News) -> Unit) {
        shareClickListener = listener
    }

    private var seeMoreClickListener: ((News) -> Unit)? = null
    fun setOnSeeMoreClickListener(listener: (News) -> Unit) {
        seeMoreClickListener = listener
    }

    interface OnVideoPreparedListener {
        fun onVideoPrepared(exoPlayerItem: ExoPlayerItem)
    }
}
