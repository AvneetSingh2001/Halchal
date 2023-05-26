package com.avicodes.halchalin.presentation.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.models.ArticleProcessed
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.databinding.FragmentUserProfileBinding
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import com.avicodes.halchalin.presentation.ui.home.ads.AdsFragmentDirections
import com.avicodes.halchalin.presentation.ui.home.ads.ArticlesAdapter
import com.avicodes.halchalin.presentation.ui.home.ads.FeaturedArticleAdapter
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UserProfileFragment : Fragment(), FeaturedArticleAdapter.FeaturedOnClickListener {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeActivityViewModel
    private lateinit var articleAdapter: FeaturedArticleAdapter

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

        viewModel = (activity as HomeActivity).viewModel
        val user = args.user
        binding.apply {
            Glide.with(ivUser.context)
                .load(user.imgUrl).circleCrop()
                .error(R.drawable.baseline_person_24)
                .into(ivUser)

            tvName.text = user.name
            tvBio.text = user.about
        }

        articleAdapter = FeaturedArticleAdapter(false, this)

        binding.rvArticles.adapter = articleAdapter
        binding.rvArticles.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        getUserArticles(user.userId)

    }


    private fun getUserArticles(userId: String) {
        viewModel.getUserArticles(userId)
        viewModel.userArticles.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Result.Error -> {
                    Toast.makeText(context, "An Error Occurred", Toast.LENGTH_LONG).show()
                    Log.e("Avneet Error", response.exception?.message.toString())
                    viewModel.userArticles.postValue(Result.NotInitialized)
                    binding.progBar.visibility = View.GONE

                }

                is Result.Success -> {
                    response.data?.let {
                        if (response.data.isNotEmpty()) {
                            binding.tv0.text = "Articles (${response.data.size})"
                            binding.rvArticles.smoothScrollToPosition(0)
                            articleAdapter.differ.submitList(response.data)
                        } else {
                            binding.tv0.text = "No Articles"
                        }
                    }
                    binding.progBar.visibility = View.GONE
                    viewModel.userArticles.postValue(Result.NotInitialized)
                }

                is Result.Loading -> {
                    binding.progBar.visibility = View.VISIBLE
                    binding.tv0.text = "Articles"
                }
                else -> {

                }
            }
        })
    }

    override fun onItemClickListener(article: ArticleProcessed) {
        TODO("Not yet implemented")
    }

    override fun onDeleteClickListener(article: ArticleProcessed) {
        TODO("Not yet implemented")
    }

}