package com.avicodes.halchalin.presentation.ui.home.reports

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.avicodes.halchalin.presentation.ui.home.reports.local.LocalNewsFragment
import com.avicodes.halchalin.presentation.ui.home.reports.remote.GlobeNewsFragment
import com.avicodes.halchalin.presentation.ui.home.reports.remote.IndiaNewsFragment

class NewsAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                LocalNewsFragment()
            }
            1 -> {
                IndiaNewsFragment()
            }
            else -> {
                GlobeNewsFragment()
            }
        }
    }
}