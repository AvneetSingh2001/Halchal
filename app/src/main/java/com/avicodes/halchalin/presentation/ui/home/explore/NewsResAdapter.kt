package com.avicodes.halchalin.presentation.ui.home.explore

import android.provider.MediaStore.Images
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.models.FeaturedAds
import com.bumptech.glide.Glide
import com.google.android.material.transition.Hold
import com.smarteist.autoimageslider.SliderViewAdapter

class NewsResAdapter(
    private val resUrl: List<String>
): SliderViewAdapter<NewsResAdapter.Holder>() {

    private var player: ExoPlayer? = null


    inner class Holder(itemView: View): ViewHolder(itemView) {
        var ivSlider: ImageView = itemView.findViewById(R.id.ivSlider)
        var videoView: PlayerView = itemView.findViewById(R.id.video_view)
    }

    override fun getCount(): Int {
        return resUrl.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): Holder {
        var view = LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_slider, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(viewHolder: Holder?, position: Int) {
        viewHolder?.apply {
            val item = resUrl[position]

            Glide.with(ivSlider.context)
                .load(item)
                .into(ivSlider)

//            player = ExoPlayer.Builder(videoView.context)
//                .build()
//                .also { exoPlayer ->
//                    videoView.player = exoPlayer
//                    val mediaItem = MediaItem.fromUri(item)
//                    exoPlayer.setMediaItem(mediaItem)
//                }
        }
    }
}
