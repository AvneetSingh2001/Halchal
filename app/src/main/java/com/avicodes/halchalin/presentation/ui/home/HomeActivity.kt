package com.avicodes.halchalin.presentation.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.avicodes.halchalin.data.utils.Result
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.avicodes.halchalin.MainActivity
import com.avicodes.halchalin.R
import com.avicodes.halchalin.databinding.ActivityHomeBinding
import com.avicodes.halchalin.presentation.CheckNetworkConnection
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: HomeActivityViewModelFactory

    lateinit var viewModel: HomeActivityViewModel

    private lateinit var checkNetworkConnection: CheckNetworkConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, viewModelFactory)[HomeActivityViewModel::class.java]

        callNetworkConnection()

        getCurUser()
        observeTopAds()
        fetchDataAds()
        fetchLocalNewss()
        fetchRemoteNationalNews()
        fetchRemoteNews()
        fetchCategories()
        observeFeatured()


        val navController = findNavController(R.id.fragmentContainerView)
        navController.setGraph(
            R.navigation.home_nav_graph
        )
        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> showBottomNav()
                R.id.localNewsFragment -> showBottomNav()
                R.id.newsFragment -> showBottomNav()
                R.id.adsFragment -> showBottomNav()
                R.id.settingsFragment -> showBottomNav()
                else -> hideBottomNav()
            }
        }
        observeTabs()

    }

    private fun observeTopAds() {
        viewModel.getAllTopAds()
    }

    private fun callNetworkConnection() {
        checkNetworkConnection = CheckNetworkConnection(application)
        checkNetworkConnection.observe(this) { isConnected ->
            if (!isConnected) {
                binding.lConnection.root.visibility = View.VISIBLE
                Glide.with(applicationContext)
                    .asGif()
                    .load(R.drawable.connection)
                    .into(
                        binding.lConnection.ivConnection
                    )
            } else {
                binding.lConnection.root.visibility = View.GONE
            }
        }
    }

    private fun fetchRemoteNationalNews() {
        lifecycleScope.launch {
            viewModel.getNationalNewsHeadlines(
                "national",
                "in",
                "hi"
            )
        }
    }

    private fun fetchRemoteNews() {
        lifecycleScope.launch {
            viewModel.getInternationalNewsHeadlines(
                topic = "world",
                country = "in",
                lang = "hi"
            )
        }
    }

    private fun observeFeatured() {

    }

    private fun getCurUser() {
        viewModel.getUserLocally()
    }

    private fun observeTabs() {
        viewModel.linkNews.observe(this, Observer { response ->
            when (response) {
                is Result.Success -> {
                    response.data?.let { screen ->
                        if (screen == "Article")
                            binding.bottomNavigation.selectedItemId = R.id.adsFragment
                        else if (screen == "News")
                            binding.bottomNavigation.selectedItemId = R.id.localNewsFragment

                        viewModel.linkNews.postValue(Result.NotInitialized)
                    }
                }

                else -> {}
            }
        })
    }

    private fun fetchDataAds() = lifecycleScope.launch(Dispatchers.IO) {
        viewModel.getFeaturedAds()
    }

    fun fetchLocalNewss() = lifecycleScope.launch(Dispatchers.IO) {
        viewModel.getLocalNews()
    }


    private fun fetchCategories() = lifecycleScope.launch(Dispatchers.IO) {
        viewModel.getCategories()
    }

    private fun showBottomNav() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.bottomNavigation.visibility = View.GONE
    }

    fun logout() {
        viewModel.logout()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val getLink = intent?.getStringExtra("deepLink")
        Log.e("Avneet New Intent", "HomeActivity $getLink")

        getLink?.let { getLink ->
            var uri = getLink.toUri()

            var newsId = uri.getQueryParameter("news")
            var articleId = uri.getQueryParameter("article")

            Log.e("Avneet uri", "\n newsId  =  $newsId \n articleId  =  $articleId")

            newsId?.let {
                viewModel.getNewsByDeepLink(it)
            }

            articleId?.let {
                viewModel.getArticleByDeepLink(it)
            }
        }
    }
}