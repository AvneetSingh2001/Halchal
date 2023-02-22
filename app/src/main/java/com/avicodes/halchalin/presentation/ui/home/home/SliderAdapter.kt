package com.avicodes.halchalin.presentation.ui.home.home

import android.provider.MediaStore.Images
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.models.FeaturedAds
import com.bumptech.glide.Glide
import com.google.android.material.transition.Hold
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter(
    private val images: List<FeaturedAds>
): SliderViewAdapter<SliderAdapter.Holder>() {
    inner class Holder(itemView: View): ViewHolder(itemView) {
        var ivSlider: ImageView = itemView.findViewById(R.id.ivSlider)
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): Holder {
        var view = LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_slider, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(viewHolder: Holder?, position: Int) {
        viewHolder?.apply {
            Glide.with(ivSlider.context)
                .load(images[position].imgUrl)
                .into(ivSlider)
        }
    }

}