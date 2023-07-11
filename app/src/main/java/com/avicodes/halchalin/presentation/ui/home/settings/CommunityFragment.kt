package com.avicodes.halchalin.presentation.ui.home.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.utils.Constants.ABOUT_US_URI
import com.avicodes.halchalin.data.utils.Constants.BUG_URI
import com.avicodes.halchalin.data.utils.Constants.JOIN_US_URI
import com.avicodes.halchalin.data.utils.Constants.REQUEST_FEATURE_URI
import com.avicodes.halchalin.databinding.FragmentCommunityBinding
import com.bumptech.glide.Glide

class CommunityFragment : Fragment() {

    private var _binding: FragmentCommunityBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCommunityBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            Glide.with(ivFooter.context)
                .load(R.drawable.tagline)
                .into(ivFooter)

            btnRequestFeature.setOnClickListener {
                val urlIntent = Intent(Intent.ACTION_VIEW)
                urlIntent.data = Uri.parse(REQUEST_FEATURE_URI)
                startActivity(urlIntent)
            }

            btnAboutUs.setOnClickListener {
                val urlIntent = Intent(Intent.ACTION_VIEW)
                urlIntent.data = Uri.parse(ABOUT_US_URI)
                startActivity(urlIntent)
            }

            btnJoinUs.setOnClickListener {
                val urlIntent = Intent(Intent.ACTION_VIEW)
                urlIntent.data = Uri.parse(JOIN_US_URI)
                startActivity(urlIntent)
            }

            btnReportBug.setOnClickListener {
                val urlIntent = Intent(Intent.ACTION_VIEW)
                urlIntent.data = Uri.parse(BUG_URI)
                startActivity(urlIntent)
            }

        }
    }

}