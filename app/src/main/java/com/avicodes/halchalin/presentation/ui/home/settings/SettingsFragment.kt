package com.avicodes.halchalin.presentation.ui.home.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.utils.Constants.APP_PACKAGE_NAME
import com.avicodes.halchalin.databinding.FragmentSettingsBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import com.bumptech.glide.Glide
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeActivityViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as HomeActivity).viewModel


        binding.run {
            btnSignOut.setOnClickListener {
                (activity as HomeActivity).logout()
            }

            btnEdit.setOnClickListener {
                navigateToEditScreen()
            }


            viewModel.curUser.observe(requireActivity(), Observer { user ->
                user?.let {
                    tvName.text = user.name
                    //tvPhone.text = user.about

                    Glide.with(ivUser)
                        .load(user.imgUrl).circleCrop()
                        .error(R.drawable.baseline_person_24)
                        .into(ivUser)


                    btnMyArticles.setOnClickListener {
                        navigateToArticlesScreen(user.userId)
                    }
                }
            })

            btnCommunity.setOnClickListener {
                val action = SettingsFragmentDirections.actionSettingsFragmentToCommunityFragment()
                requireView().findNavController().navigate(action)
            }

            btnRateUs.setOnClickListener {
                rateApp()
            }
        }
    }

    private fun rateApp() {
        context?.let { context ->
            activity?.let { activity ->
                val manager = ReviewManagerFactory.create(context)
                val request = manager.requestReviewFlow()
                request.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // We got the ReviewInfo object
                        val reviewInfo = task.result
                        val flow = manager.launchReviewFlow(activity, reviewInfo)
                        flow.addOnCompleteListener { _ -> }
                    } else {
                        rateAppFromPlayStore()
                    }
                }.addOnFailureListener {
                    rateAppFromPlayStore()
                }
            }
        }
    }

    private fun rateAppFromPlayStore() {
        val uri: Uri = Uri.parse("market://details?id=$APP_PACKAGE_NAME")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=$APP_PACKAGE_NAME")
                )
            )
        }
    }


    private fun navigateToArticlesScreen(userId: String) {
        val action = SettingsFragmentDirections.actionSettingsFragmentToAllArticlesFragment(userId)
        requireView().findNavController().navigate(action)
    }

    private fun navigateToEditScreen() {
        val action = SettingsFragmentDirections.actionSettingsFragmentToEditFragment()
        requireView().findNavController().navigate(action)
    }
}