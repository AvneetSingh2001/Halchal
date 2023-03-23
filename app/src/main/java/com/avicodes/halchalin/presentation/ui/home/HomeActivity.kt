package com.avicodes.halchalin.presentation.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.avicodes.halchalin.data.utils.Result
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.avicodes.halchalin.MainActivity
import com.avicodes.halchalin.R
import com.avicodes.halchalin.databinding.ActivityHomeBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel = ViewModelProvider(this, viewModelFactory)[HomeActivityViewModel::class.java]


        getCurUser()
        fetchDataAds()
        fetchLocalNewss()
        fetchRemoteNews()

        val newsLink = intent.getStringExtra("deepLink")
        newsLink?.let {
            viewModel.getNewsByDeepLink(it)
        }

        val navController  = findNavController(R.id.fragmentContainerView)
        navController.setGraph(
            R.navigation.home_nav_graph)
        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> showBottomNav()
                R.id.newsVpFragment -> showBottomNav()
                R.id.newsFragment -> showBottomNav()
                R.id.adsFragment -> showBottomNav()
                R.id.settingsFragment -> showBottomNav()
                else -> hideBottomNav()
            }
        }
        observeTabs()
    }

    private fun getCurUser() {
        viewModel.getUserLocally()
    }

    private fun observeTabs() {
        viewModel.exploreNewsTab.observe(this, Observer { response ->
            when(response) {
                is Result.Success -> {
                    binding.bottomNavigation.selectedItemId = R.id.newsVpFragment
                }
                else -> {}
            }
        })
    }

    private fun fetchDataAds() = lifecycleScope.launch(Dispatchers.IO){
        viewModel.getFeaturedAds()
    }

    fun fetchLocalNewss() = lifecycleScope.launch(Dispatchers.IO) {
        viewModel.getLocalNews()
    }

    private fun fetchRemoteNews() = lifecycleScope.launch(Dispatchers.IO) {
        viewModel.getNationalNewsHeadlines("IN", "hi")
        viewModel.getWorldNewsHeadlines("WORLD", "IN", "hi")
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
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }
}