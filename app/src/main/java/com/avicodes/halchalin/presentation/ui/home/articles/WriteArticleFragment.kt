package com.avicodes.halchalin.presentation.ui.home.articles

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isNotEmpty
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.utils.Constants
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.databinding.FragmentWriteArticelBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageActivity
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import kotlinx.coroutines.launch


class WriteArticleFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentWriteArticelBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeActivityViewModel

    private var imageUri: Uri? = null
    val notPicked: Boolean = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWriteArticelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as HomeActivity).viewModel

        binding.apply {
            postButton.setOnClickListener {
                val check1 = tvTitle.editText?.text?.isNotEmpty() ?: false
                val check2 = tvDesc.editText?.text?.isNotEmpty() ?: false
                val check3 = tvTag.editText?.text?.isNotEmpty() ?: false
                if (imageUri != null) {
                    if (check1 and check2) {
                        val userId = viewModel.curUser.value?.userId
                        userId?.let {
                            lifecycleScope.launch {
                                viewModel.uploadArticle(
                                    title = tvTitle.editText?.text.toString(),
                                    desc = tvDesc.editText?.text.toString(),
                                    tag = if (check3) tvTag.editText?.text.toString() else "null",
                                    imgUri = imageUri!!,
                                    userId = userId,
                                    enableComment = cbEnableComment.isChecked
                                )
                            }
                        }
                    } else if (!check1) {
                        Toast.makeText(context, "Please write title", Toast.LENGTH_LONG).show()
                    } else if (!check2) {
                        Toast.makeText(context, "Please write article", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(context, "Please upload image", Toast.LENGTH_LONG).show()
                }
            }
            uploadImageButton.setOnClickListener {
                if (hasStoragePermission())
                    selectImage()
                else
                    requestStoragePermission()
            }
            observeUpload()
        }
    }

    fun toggleButton() {
        binding.uploadImageButton.text = if (notPicked) {
            "Add Image"
        } else {
            "Change Image"
        }
    }

    private fun observeUpload() {
        binding.apply {
            viewModel.articleUploaded.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is Result.Success -> {
                        hideLoader()
                        viewModel.articleUploaded.postValue(Result.NotInitialized)
                        Toast.makeText(context, "Successfully Uploaded", Toast.LENGTH_LONG).show()
                        findNavController().popBackStack()
                    }

                    is Result.Error -> {
                        Log.e("Avneet ", "Not uploaded: ${it.exception}")
                        hideLoader()
                        Toast.makeText(context, "Try Again...", Toast.LENGTH_LONG).show()
                    }

                    is Result.Loading -> {
                        showLoader()
                    }

                    is Result.NotInitialized -> {}
                }
            })
        }
    }

    fun showLoader() {
        binding.apply {
            progCons.visibility = View.VISIBLE
            mainCons.visibility = View.INVISIBLE
        }
    }

    fun hideLoader() {
        binding.apply {
            progCons.visibility = View.GONE
            mainCons.visibility = View.VISIBLE
        }
    }

    private val changeImage =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                val imgUri = data?.data
                imageUri = imgUri
                binding.apply {
                    ivImage.visibility = View.VISIBLE
                    Glide.with(
                        ivImage.context
                    ).load(imageUri).into(ivImage)
                    toggleButton()
                }
            } else {
                Log.e("Avneet ", "Not uploaded")
            }
        }


    private fun selectImage() {
        cropImage.launch(
            CropImageContractOptions(
                uri = null,
                CropImageOptions(
                    allowRotation = true,
                    allowFlipping = true,
                    cropMenuCropButtonTitle = "CROP",
                    imageSourceIncludeGallery = true,
                    imageSourceIncludeCamera = false,
                    )
            )
        )
        //selectPictureLauncher.launch(intent.type)
    }



    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // Use the returned uri.
            val it = result.uriContent

            binding.apply {
                ivImage.visibility = View.VISIBLE
                Glide.with(
                    this@WriteArticleFragment
                ).asBitmap().centerInside().placeholder(R.drawable.image_placeholder)
                    .skipMemoryCache(true).load(it).into(ivImage)
            }
            toggleButton()
            imageUri = it
        } else {
            // An error occurred.
            val exception = result.error
        }

    }


    private fun hasStoragePermission() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            EasyPermissions.requestPermissions(
                this,
                "Please provide access to gallery",
                Constants.PERMISSION_READ_STORAGE_REQUEST_CODE,
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "Please provide access to gallery",
                Constants.PERMISSION_READ_STORAGE_REQUEST_CODE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestStoragePermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(
            requireContext(),
            "Permission Granted!",
            Toast.LENGTH_SHORT
        ).show()
        selectImage()
    }

}