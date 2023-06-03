package com.avicodes.halchalin.presentation.ui.auth.phone

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.avicodes.halchalin.R
import com.avicodes.halchalin.databinding.ActivityMainBinding
import com.avicodes.halchalin.presentation.CheckNetworkConnection
import com.avicodes.halchalin.presentation.openDialog
import com.avicodes.halchalin.presentation.ui.NoInternetDialogFragment
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private lateinit var checkNetworkConnection: CheckNetworkConnection

    @Inject
    lateinit var viewModelFactory: MainActivityViewModelFactory

    lateinit var viewModel: MainActivityViewModel

    private var isNoInternetShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainActivityViewModel::class.java]

        callNetworkConnection()

        lifecycleScope.launch {
            viewModel.isLoggedIn().collectLatest {
                Log.e("login", it.toString())
                if(it){
                    binding.progBar.visibility = View.VISIBLE
                    binding.navHostFragment.visibility = View.INVISIBLE
                    moveToHomeActivity()
                }else {

                    binding.progBar.visibility = View.GONE
                    binding.navHostFragment.visibility = View.VISIBLE
                }
            }
        }

        supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    }


    private fun callNetworkConnection() {
        if (!isNetworkAvailable()) {
            if (!isNoInternetShown) {
                isNoInternetShown = true
                val dialogNoInternet = NoInternetDialogFragment {
                    isNoInternetShown = false
                    callNetworkConnection()
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

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }



    fun moveToHomeActivity() {
        getDynamicLink()
    }

    fun getDynamicLink() {
        Firebase.dynamicLinks
            .getDynamicLink(intent).addOnCompleteListener {

            }
            .addOnSuccessListener(this) { pendingDynamicLinkData: PendingDynamicLinkData? ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                }

                Log.e("DeepLink: ", deepLink.toString())
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                intent.putExtra("deepLink", deepLink.toString())
                startActivity(intent)
                finish()

            }
            .addOnFailureListener(this) { e ->
                Log.w(TAG, "getDynamicLink:onFailure", e)
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
            }
    }

}