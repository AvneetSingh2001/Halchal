package com.avicodes.halchalin.presentation.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.avicodes.halchalin.R
import com.avicodes.halchalin.databinding.FragmentUserProfileBinding
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UserProfileFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    val args: UserProfileFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = args.user
        binding.apply {
            Glide.with(ivUser.context)
                .load(user.imgUrl).circleCrop()
                .error(R.drawable.baseline_person_24)
                .into(ivUser)

            tvName.text = user.name
            tvBio.text = user.about
        }
    }

}