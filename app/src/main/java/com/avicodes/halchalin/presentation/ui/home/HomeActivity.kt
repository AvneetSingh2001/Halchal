package com.avicodes.halchalin.presentation.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.avicodes.halchalin.data.utils.Result
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.avicodes.halchalin.presentation.ui.auth.MainActivity
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.utils.Constants
import com.avicodes.halchalin.databinding.ActivityHomeBinding
import com.avicodes.halchalin.presentation.CheckNetworkConnection
import com.avicodes.halchalin.presentation.openDialog
import com.avicodes.halchalin.presentation.ui.NoInternetDialogFragment
import com.avicodes.halchalin.presentation.ui.auth.providers.google.GoogleAuthActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.vmadalin.easypermissions.EasyPermissions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    private var isNoInternetShown = false

    private var mInterstitialAd: InterstitialAd? = null
    var tabCount = 0

    @Inject
    lateinit var viewModelFactory: HomeActivityViewModelFactory

    lateinit var viewModel: HomeActivityViewModel

    private lateinit var checkNetworkConnection: CheckNetworkConnection
    private lateinit var appUpdateManager: AppUpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadInterstitialAd()
        checkForUpdate()

        if(!hasNotificationPermission())  {
            requestNotificationPermission()
        }

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
        viewModel.curUser.observe(this, Observer {
            viewModel.getLocalNews(it.location.toString())
        })


        val navController = findNavController(R.id.fragmentContainerView)
        navController.setGraph(
            R.navigation.home_nav_graph
        )
        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> showBottomNav()
                R.id.localNewsFragment -> {
                    showBottomNav()
                }

                R.id.newsFragment -> {
                    showBottomNav()
                    tabCount++
                    showAd()

                }

                R.id.adsFragment -> {
                    showBottomNav()
                    tabCount++
                    showAd()
                }

                R.id.settingsFragment -> showBottomNav()
                else -> hideBottomNav()
            }
        }

        observeTabs()

    }

    private fun showAd() {
        if (tabCount > 2) {
            tabCount = 0
            if (mInterstitialAd != null) {
                mInterstitialAd?.show(this)
                loadInterstitialAd()
            } else {
                loadInterstitialAd()
            }
        }
    }

    private fun loadInterstitialAd() {
        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            this,
            "ca-app-pub-5283250244975535/1432877536",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                }
            })
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


    private fun hasNotificationPermission() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            EasyPermissions.hasPermissions(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            )
        } else {
            false
        }


    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            EasyPermissions.requestPermissions(
                this,
                "Get updates about Halchal over notifications",
                Constants.PERMISSION_NOTFICATION_REQUEST_CODE,
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
    }

    private fun checkForUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this)

        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        // Checks that the platform will allow the specified type of update.
        // Checks whether the platform allows the specified type of update,
// and checks the update priority.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.updatePriority() >= 5 /* high priority */
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request an immediate update.
                appUpdateManager.startUpdateFlowForResult(
                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                    appUpdateInfo,
                    // an activity result launcher registered via registerForActivityResult
                    activityResultLauncher,
                    // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                    // flexible updates.
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
                        .build()
                )
            }
        }
    }


    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result: ActivityResult ->
        // handle callback
        if (result.resultCode != RESULT_OK) {
            Log.e("Update flow failed! ","Result Code" +  result.resultCode.toString());
            // If the update is cancelled or fails,
            // you can request to start the update again.
        }
    }


    override fun onResume() {
        super.onResume()

        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    // If an in-app update is already running, resume the update.
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        activityResultLauncher,
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build())
                }
            }
    }

    private fun showBottomNav() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.bottomNavigation.visibility = View.GONE
    }

    fun logout() {
        viewModel.logout()
        val intent = Intent(this, GoogleAuthActivity::class.java)
        startActivity(intent)
        finish()
    }

}