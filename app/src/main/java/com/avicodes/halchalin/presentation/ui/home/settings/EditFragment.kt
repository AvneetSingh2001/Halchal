package com.avicodes.halchalin.presentation.ui.home.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.databinding.FragmentEditBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import com.bumptech.glide.Glide
import com.avicodes.halchalin.data.utils.Result


class EditFragment : Fragment() {

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageUrl : String
    private lateinit var viewModel: HomeActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as HomeActivity).viewModel

        binding.apply {
            btnImage.setOnClickListener {
                selectImage()
            }

            viewModel.curUser.observe(requireActivity(), Observer { u ->
                u?.let {
                    etName.editText?.setText(u.name)
                    etAbout.editText?.setText(u.about)

                    Glide.with(ivUser.context)
                        .load(u.imgUrl)
                        .error(R.drawable.baseline_person_24)
                        .into(ivUser)

                    imageUrl = u.imgUrl
                    etPhone.editText?.setText(u.mobile)
                }
            })

            btnSubmit.setOnClickListener {
                viewModel.saveUserLocally(
                    name = etName.editText?.text.toString(),
                    phone = etPhone.editText?.text.toString(),
                    about = etAbout.editText?.text.toString(),
                    image = imageUrl
                )
                requireView().findNavController().popBackStack()
            }

        }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        selectPictureLauncher.launch(intent.type)
    }

    private val selectPictureLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){
        viewModel.saveUserImage(it.toString())
        viewModel.updateUserPic.observe(requireActivity(), Observer {result ->
            when(result) {
                is Result.Success -> {
                    Glide.with(requireContext()).load(result.data.toString()).circleCrop().error(
                        R.drawable.baseline_person_24).into(binding.ivUser)
                    imageUrl = result.data.toString()
                    hideProg()
                }
                is Result.Loading -> {
                    showProg()
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), "Error, Try Again", Toast.LENGTH_SHORT).show()
                    hideProg()
                }
                else -> {
                    hideProg()
                }
            }
        })
    }

    fun showProg() {
        binding.progCons.visibility = View.VISIBLE
        binding.mainCons.visibility = View.INVISIBLE
    }

    fun hideProg() {
        binding.progCons.visibility = View.INVISIBLE
        binding.mainCons.visibility = View.VISIBLE
    }
}