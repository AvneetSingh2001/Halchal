package com.avicodes.halchalin.presentation.ui

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.avicodes.halchalin.databinding.ActivitySplashBinding
import com.avicodes.halchalin.presentation.ui.auth.MainActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: SplashViewModelFactory

    lateinit var viewModel: SplashViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, viewModelFactory)[SplashViewModel::class.java]

        lifecycleScope.launch {
            viewModel.isLoggedIn().collectLatest {
                Log.e("login", it.toString())
                if (it) {
                    delay(2000)
                    moveToHomeActivity()
                } else {
                    delay(2000)
                    moveToSignUpActivity()
                }
            }
        }

    }

    private fun moveToSignUpActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
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
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish()
            }
            .addOnFailureListener(this) { e ->
                Log.w(ContentValues.TAG, "getDynamicLink:onFailure", e)
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
            }
    }
}