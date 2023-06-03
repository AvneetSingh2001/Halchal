package com.avicodes.halchalin.presentation.ui

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.avicodes.halchalin.R
import com.avicodes.halchalin.databinding.FragmentNoInternetDialogBinding
import com.bumptech.glide.Glide

class NoInternetDialogFragment(
    private val dismissCallback: () -> Unit
) : DialogFragment() {

    private var binding: FragmentNoInternetDialogBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoInternetDialogBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    companion object {
        const val TAG = "NoInternetDialogFragment"
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.let {
            it.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            it.window!!.setBackgroundDrawable(ColorDrawable(0))
        }



        binding?.apply {

            context?.let {
                Glide.with(it)
                    .asGif()
                    .load(R.drawable.connection)
                    .into(
                        ivConnection
                    )
            }

            btnRetry.setOnClickListener {
                dismissCallback()
                dismiss()
            }
        }
    }

}