package com.avicodes.halchalin.presentation.ui.home.explore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.avicodes.halchalin.R
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter

class NewsResAdapter(
    private val resUrl: List<String>
) : SliderViewAdapter<NewsResAdapter.Holder>() {

    inner class Holder(itemView: View) : ViewHolder(itemView) {
        var ivSlider: ImageView = itemView.findViewById(R.id.ivSlider)
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
        }
    }
}
