package com.avicodes.halchalin.presentation.ui.home.explore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.avicodes.halchalin.R
import com.avicodes.halchalin.databinding.FragmentCommentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CommentFragment : BottomSheetDialogFragment() {

    private var _binding : FragmentCommentBinding? = null
    private val binding get() = _binding!!

    val args: CommentFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newsId = args.news

        binding.apply {
            sendButton.setOnClickListener {
                val comment = etComment.editText?.text.toString()
                if(comment != " ") {

                }
            }
        }

    }
}