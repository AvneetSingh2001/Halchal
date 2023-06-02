package com.avicodes.halchalin.presentation.ui.home.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.models.City
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.databinding.FragmentEditBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import com.avicodes.halchalin.presentation.ui.home.reports.NewsFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import java.lang.reflect.TypeVariable
import java.util.*


class EditFragment(
) : Fragment() {

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageUrl: String
    private lateinit var viewModel: HomeActivityViewModel

    private val GALLERY_REQUEST_CODE = 1234
    private val WRITE_EXTERNAL_STORAGE_CODE = 1

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    lateinit var userPicUri: Uri

    private var citiesList: List<String> = mutableListOf()
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

            getCities()

            btnImage.setOnClickListener {
                selectImage()
            }

            viewModel.curUser.observe(requireActivity(), Observer { u ->
                u?.let {
                    etName.editText?.setText(u.name)
                    etAbout.editText?.setText(u.about)

                    Glide.with(ivUser.context)
                        .load(u.imgUrl).circleCrop()
                        .error(R.drawable.baseline_person_24)
                        .placeholder(CircularProgressDrawable(ivUser.context))
                        .into(ivUser)

                    etLoc.editText?.setText(u.location)
                    imageUrl = u.imgUrl
                    etPhone.editText?.setText(u.mobile)
                }
            })

            btnSubmit.setOnClickListener {
                var city = etLoc.editText?.text.toString()
                viewModel.saveUser(
                    name = etName.editText?.text.toString(),
                    phone = etPhone.editText?.text.toString(),
                    about = etAbout.editText?.text.toString(),
                    image = imageUrl,
                    location = city
                )
                requireView().findNavController().popBackStack()
            }

            etLoc.addOnEditTextAttachedListener {
                etLoc.error = null
            }
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        cropImage.launch(
            CropImageContractOptions(
                uri = null,
                CropImageOptions(
                    allowRotation = true,
                    allowFlipping = true,
                    cropMenuCropButtonTitle = "CROP",
                    imageSourceIncludeGallery = true,
                )
            )
        )
    }

    private val selectPictureLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            cropImage.launch(
                CropImageContractOptions(
                    uri = it,
                    CropImageOptions(
                        allowRotation = true,
                        allowFlipping = true,
                        cropMenuCropButtonTitle = "CROP"
                    )
                )
            )
        }


    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // Use the returned uri.
            val it = result.uriContent



            viewModel.saveUserImage(it.toString())
            viewModel.updateUserPic.observe(requireActivity(), Observer { result ->
                when (result) {
                    is Result.Success -> {
                        imageUrl = result.data.toString()

                        Glide.with(binding.ivUser.context)
                            .load(it).circleCrop()
                            .error(R.drawable.baseline_person_24)
                            .placeholder(CircularProgressDrawable(binding.ivUser.context))
                            .into(binding.ivUser)

                        hideProg()
                    }
                    is Result.Loading -> {
                        showProg()
                    }
                    is Result.Error -> {
                        Toast.makeText(requireContext(), "Error, Try Again", Toast.LENGTH_SHORT)
                            .show()
                        hideProg()
                    }
                    else -> {
                        hideProg()
                    }
                }
            })

        } else {
            // An error occurred.
            val exception = result.error
        }
    }


    fun showProg() {
        binding.progCons.visibility = View.VISIBLE
        binding.mainCons.visibility = View.INVISIBLE
    }

    fun hideProg() {
        binding.progCons.visibility = View.INVISIBLE
        binding.mainCons.visibility = View.VISIBLE
    }

    fun getCities() {
        viewModel.getCities().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Result.Success -> {
                    citiesList = it.data?.map(City::name)!!
                    Log.e("Cities Fetch", it.data.toString())
                    val adapter = ArrayAdapter(requireContext(), R.layout.item_city, citiesList)
                    (binding.etLoc.editText as? AutoCompleteTextView)?.setAdapter(adapter)
                }

                is Result.Error -> {
                    Log.e("Cities Fetch", it.exception?.message.toString())
                    val items = listOf("Error Fetching Data")
                    val adapter = ArrayAdapter(requireContext(), R.layout.item_city, items)
                    (binding.etLoc.editText as? AutoCompleteTextView)?.setAdapter(adapter)

                }
                is Result.Loading -> {
                    val items = listOf("Loading...")
                    val adapter = ArrayAdapter(requireContext(), R.layout.item_city, items)
                    (binding.etLoc.editText as? AutoCompleteTextView)?.setAdapter(adapter)
                }
                else -> {}
            }
        })
    }

}

