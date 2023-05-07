package com.avicodes.halchalin.presentation.ui.home.ads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.avicodes.halchalin.R
import com.avicodes.halchalin.databinding.FragmentAdsBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AdsFragment : Fragment() {

    private var _binding: FragmentAdsBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAdsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            context?.let {
                Glide.with(it)
                    .asGif()
                    .load("https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExMTNlODRiNzFjMWVlYTk2MTk1YTkzOTI1OTgxNTlhMDI2NTUyNGZhMiZlcD12MV9pbnRlcm5hbF9naWZzX2dpZklkJmN0PWc/3o7TKnnTyK6tYMNAxW/giphy.gif")
                    .into(
                        progressCircular.ivLoader
                    )
            }
        }
    }

}