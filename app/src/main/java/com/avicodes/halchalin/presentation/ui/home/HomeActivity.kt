package com.avicodes.halchalin.presentation.ui.home

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
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
import com.avicodes.halchalin.presentation.ui.auth.phone.MainActivity
import com.avicodes.halchalin.R
import com.avicodes.halchalin.databinding.ActivityHomeBinding
import com.avicodes.halchalin.presentation.CheckNetworkConnection
import com.avicodes.halchalin.presentation.openDialog
import com.avicodes.halchalin.presentation.ui.NoInternetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    private var isNoInternetShown = false

    @Inject
    lateinit var viewModelFactory: HomeActivityViewModelFactory

    lateinit var viewModel: HomeActivityViewModel

    private lateinit var checkNetworkConnection: CheckNetworkConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, viewModelFactory)[HomeActivityViewModel::class.java]

        val getLink = intent?.getStringExtra("deepLink")

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

        callNetworkConnection()
        checkConnection()
        getCurUser()


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

    private fun checkConnection() {
        checkNetworkConnection = CheckNetworkConnection(application)
        checkNetworkConnection.observe(this) { isConnected ->
            if (!isConnected) {
                callNetworkConnection()
            }
        }
    }


    private fun callNetworkConnection() {
        if (!isNetworkAvailable()) {
            if (!isNoInternetShown) {
                isNoInternetShown = true
                val dialogNoInternet = NoInternetDialogFragment {
                    isNoInternetShown = false
                }
                openDialog(dialogNoInternet, NoInternetDialogFragment.TAG)
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
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

}