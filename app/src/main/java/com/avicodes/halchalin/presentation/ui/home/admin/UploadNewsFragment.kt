package com.avicodes.halchalin.presentation.ui.home.admin


import android.Manifest
import com.avicodes.halchalin.R
import com.avicodes.halchalin.databinding.FragmentUploadNewsBinding
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Build
import com.avicodes.halchalin.data.utils.Result
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.avicodes.halchalin.data.models.City
import com.avicodes.halchalin.data.utils.Constants
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import com.avicodes.halchalin.presentation.ui.home.admin.adapter.UploadedImagesAdapter
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vmadalin.easypermissions.EasyPermissions
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class UploadNewsFragment : Fragment() {

    private var _binding: FragmentUploadNewsBinding? = null
    private val binding get() = _binding

    private lateinit var uploadedImagesAdapter: UploadedImagesAdapter

    private val viewModel by activityViewModels<HomeActivityViewModel>()

    val OPEN_MEDIA_PICKER = 1

    private var citiesList: List<String> = mutableListOf()
    private var videoUri: Uri? = null
    private var uploadedImages: List<Uri> = mutableListOf()
    private var coverUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUploadNewsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!hasStoragePermission()) {
            requestStoragePermission()
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("are you sure want to go back?")
                        // Respond to neutral button press
                        .setNegativeButton("No") { dialog, which ->
                            // Respond to negative button press
                        }
                        .setPositiveButton("Yes") { dialog, which ->
                            dialog.dismiss()
                            requireView().findNavController().popBackStack()
                        }
                        .show()
                }
            })

        binding?.apply {
            uploadedImagesAdapter = UploadedImagesAdapter()
            rvImages.adapter = uploadedImagesAdapter

            viewModel.getCities().observe(viewLifecycleOwner, Observer {
                when (it) {
                    is Result.Success -> {
                        citiesList = it.data?.map(City::name)!!
                        Log.e("Cities Fetch", it.data.toString())
                        val adapter = ArrayAdapter(requireContext(), R.layout.item_city, citiesList)
                        (etLoc.editText as? AutoCompleteTextView)?.setAdapter(adapter)
                    }

                    else -> {
                        val items = listOf("Loading...")
                        val adapter = ArrayAdapter(requireContext(), R.layout.item_city, items)
                        (etLoc.editText as? AutoCompleteTextView)?.setAdapter(adapter)
                    }
                }
            })

            btnUploadImage.setOnClickListener {
                selectImages()
            }

            btnUploadVideo.setOnClickListener {
                selectVideo()
            }

            btnUploadCover.setOnClickListener {
                selectCover()
            }

            btnUpload.setOnClickListener {
                uploadNews()
            }
        }


    }

    private fun uploadNews() {
        binding?.apply {
            lifecycleScope.launch {
                viewModel.uploadNews(
                    etNewsHeadline.editText?.text.toString(),
                    etNewsDescription.editText?.text.toString(),
                    etLoc.editText?.text.toString(),
                    videoUri,
                    uploadedImages,
                    coverUri
                ).collectLatest {
                    when (it) {
                        is Result.Success -> {
                            hideLoader()
                            Toast.makeText(requireContext(), "News Uploaded", Toast.LENGTH_SHORT)
                                .show()
                            view?.findNavController()?.popBackStack()
                        }

                        is Result.Loading -> {
                            showLoader()
                        }

                        is Result.Error -> {
                            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                            hideLoader()
                        }

                        else -> {}
                    }
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //data.getParcelableArrayExtra(name);
                //If Single image selected then it will fetch from Gallery
                data?.let {
                    if (it.data != null) {
                        val mImageUri = data.data

                        mImageUri?.let {
                            val mArrayUri = ArrayList<Uri>()
                            uploadedImages = mArrayUri
                            mArrayUri.add(mImageUri)
                            uploadedImagesAdapter.differ.submitList(uploadedImages)
                        }

                    } else if (it.clipData != null) {
                        val mClipData = it.clipData
                        val mArrayUri = ArrayList<Uri>()
                        for (i in 0 until mClipData!!.itemCount) {
                            val item = mClipData!!.getItemAt(i)
                            val uri = item.uri
                            mArrayUri.add(uri)
                        }
                        uploadedImages = mArrayUri
                        uploadedImagesAdapter.differ.submitList(uploadedImages)
                    } else {

                    }
                }
            }
        }
    }

    private fun selectVideo() {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        selectPictureLauncher.launch(intent.type)
    }

    private fun showLoader() {
        binding?.run {
            mainCons.visibility = View.INVISIBLE
            progCons.visibility = View.VISIBLE
        }
    }

    private fun hideLoader() {
        binding?.run {
            mainCons.visibility = View.VISIBLE
            progCons.visibility = View.GONE
        }
    }

    private fun selectCover() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        selectThumbnailLauncher.launch(intent.type)
    }

    private fun selectImages() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
    }

    private val selectThumbnailLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            binding?.apply {
                val mediaController = android.widget.MediaController(requireContext())
                mediaController.setAnchorView(videoView)
                ivCover.visibility = View.VISIBLE
                Glide.with(requireContext()).load(it).into(ivCover)
                coverUri = it
            }
        }

    private val selectPictureLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            binding?.apply {
                val mediaController = android.widget.MediaController(requireContext())
                mediaController.setAnchorView(videoView)
                videoView.visibility = View.VISIBLE
                videoView.setMediaController(mediaController)
                videoView.setVideoURI(it)
                videoView.requestFocus()
                videoView.setOnPreparedListener {
                    videoView.start()
                }
                videoUri = it
            }
        }

    private fun hasStoragePermission() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES
            ) && EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.READ_MEDIA_VIDEO
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
                "Please provide access to Images",
                Constants.PERMISSION_READ_STORAGE_REQUEST_CODE,
                Manifest.permission.READ_MEDIA_IMAGES
            )
            EasyPermissions.requestPermissions(
                this,
                "Please provide access to Video",
                Constants.PERMISSION_READ_STORAGE_REQUEST_CODE,
                Manifest.permission.READ_MEDIA_VIDEO
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

}