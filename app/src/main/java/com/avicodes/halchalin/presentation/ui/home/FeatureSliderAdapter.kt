package com.avicodes.halchalin.presentation.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.models.FeatureSliderModel
import com.avicodes.halchalin.databinding.FragmentNewsVpBinding
import com.avicodes.halchalin.databinding.SliderLayoutBinding
import com.bumptech.glide.Glide

class FeatureSliderAdapter(
    var imgList: ArrayList<FeatureSliderModel>,
    var context: Context
):RecyclerView.Adapter<FeatureSliderAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = SliderLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return imgList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(imgList[position])
    }

    inner class ViewHolder(private val binding: SliderLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(featuredImg: FeatureSliderModel) {
            binding.apply {
                Glide.with(context)
                    .load(featuredImg.img)
                    .into(ivFeature)
            }
        }
    }

}