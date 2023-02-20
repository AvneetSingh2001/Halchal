package com.avicodes.halchalin.presentation.ui.home.home

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.avicodes.halchalin.data.models.FeatureSliderModel
import com.avicodes.halchalin.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var featuredImg : ArrayList<FeatureSliderModel>
    private lateinit var featuredImgAdapter: FeatureSliderAdapter

    val sliderHandler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        featuredImg = ArrayList<FeatureSliderModel>()
        featuredImgAdapter = FeatureSliderAdapter(featuredImg, requireContext())
        getFeaturedImgs()
        featuredImgAdapter.notifyDataSetChanged()
        setUpFeaturedViewPager()
    }


    fun setUpFeaturedViewPager() {
        binding.vpFeatures.apply {
            adapter = featuredImgAdapter
            clipChildren = false
            clipToPadding = false
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            var compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer(MarginPageTransformer(30))
            compositePageTransformer.addTransformer(object: ViewPager2.PageTransformer{
                override fun transformPage(page: View, position: Float) {
                    val r = 1 - abs(position)
                    page.scaleY = 0.85f + r * 0.15f
                }
            })

            setPageTransformer(compositePageTransformer)
            registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    sliderHandler.removeCallbacks(sliderRunnable)
                    sliderHandler.postDelayed(sliderRunnable, 3000)
                    if(position == featuredImg.size - 2) {

                    }
                }
            })
        }
    }
    fun getFeaturedImgs() {
        featuredImg.add(
            FeatureSliderModel(
                id = 0,
                img = "https://scontent.fdel14-1.fna.fbcdn.net/v/t39.30808-6/328966125_642975040965233_6195880955684837629_n.jpg?_nc_cat=103&ccb=1-7&_nc_sid=e3f864&_nc_ohc=yF7OErU9JJkAX_3dgn7&_nc_ht=scontent.fdel14-1.fna&oh=00_AfA2017Hoktb4c4LhXD-gXhoExuqeZvaLfSe0ShOcR-Gag&oe=63F224C4"
            )
        )

        featuredImg.add(
            FeatureSliderModel(
                id = 1,
                img = "https://scontent.fdel14-1.fna.fbcdn.net/v/t39.30808-6/328966125_642975040965233_6195880955684837629_n.jpg?_nc_cat=103&ccb=1-7&_nc_sid=e3f864&_nc_ohc=yF7OErU9JJkAX_3dgn7&_nc_ht=scontent.fdel14-1.fna&oh=00_AfA2017Hoktb4c4LhXD-gXhoExuqeZvaLfSe0ShOcR-Gag&oe=63F224C4"
            )
        )

        featuredImg.add(
            FeatureSliderModel(
                id = 2,
                img = "https://scontent.fdel14-1.fna.fbcdn.net/v/t39.30808-6/328966125_642975040965233_6195880955684837629_n.jpg?_nc_cat=103&ccb=1-7&_nc_sid=e3f864&_nc_ohc=yF7OErU9JJkAX_3dgn7&_nc_ht=scontent.fdel14-1.fna&oh=00_AfA2017Hoktb4c4LhXD-gXhoExuqeZvaLfSe0ShOcR-Gag&oe=63F224C4"
            )
        )

        featuredImg.add(
            FeatureSliderModel(
                id = 3,
                img = "https://scontent.fdel14-1.fna.fbcdn.net/v/t39.30808-6/328966125_642975040965233_6195880955684837629_n.jpg?_nc_cat=103&ccb=1-7&_nc_sid=e3f864&_nc_ohc=yF7OErU9JJkAX_3dgn7&_nc_ht=scontent.fdel14-1.fna&oh=00_AfA2017Hoktb4c4LhXD-gXhoExuqeZvaLfSe0ShOcR-Gag&oe=63F224C4"
            )
        )

        featuredImg.add(
            FeatureSliderModel(
                id = 4,
                img = "https://scontent.fdel14-1.fna.fbcdn.net/v/t39.30808-6/328966125_642975040965233_6195880955684837629_n.jpg?_nc_cat=103&ccb=1-7&_nc_sid=e3f864&_nc_ohc=yF7OErU9JJkAX_3dgn7&_nc_ht=scontent.fdel14-1.fna&oh=00_AfA2017Hoktb4c4LhXD-gXhoExuqeZvaLfSe0ShOcR-Gag&oe=63F224C4"
            )
        )

        featuredImg.add(
            FeatureSliderModel(
                id = 5,
                img = "https://scontent.fagr1-4.fna.fbcdn.net/v/t39.30808-6/328966125_642975040965233_6195880955684837629_n.jpg?_nc_cat=103&ccb=1-7&_nc_sid=e3f864&_nc_ohc=iZlPkPSEWsEAX_nOsZs&_nc_ht=scontent.fagr1-4.fna&oh=00_AfA23gK5kuEJJjOt2cobmeUfVEWQGvyLFfNBv9r1VgxdMA&oe=63E252C4"
            )
        )

    }

    val sliderRunnable = Runnable {
        binding.vpFeatures.currentItem = binding.vpFeatures.currentItem + 1
    }

    val runnable = Runnable {
        featuredImg.addAll(featuredImg)
        featuredImgAdapter.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 5000)
    }

}