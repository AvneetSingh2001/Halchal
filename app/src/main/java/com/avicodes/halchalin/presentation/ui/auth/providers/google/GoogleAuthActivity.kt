package com.avicodes.halchalin.presentation.ui.auth.providers.google

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call.Details
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.utils.Constants.PRIVACY_POLICY_URI
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.databinding.ActivityGoogleAuthBinding
import com.avicodes.halchalin.presentation.ui.auth.details.DetailsActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GoogleAuthActivity : AppCompatActivity() {

    private var _binding: ActivityGoogleAuthBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: GoogleAuthViewModelFactory

    lateinit var viewModel: GoogleAuthActivityViewModel

    private lateinit var googleSignInClient: GoogleSignInClient

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityGoogleAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel =
            ViewModelProvider(this, viewModelFactory)[GoogleAuthActivityViewModel::class.java]

        auth.signOut()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.revokeAccess()

        binding.apply {
            btnGoogleSignIn.setOnClickListener {
                signInRequest()
                showLoader()
            }

            btnPrivacyPolicy.setOnClickListener {
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(PRIVACY_POLICY_URI)
                startActivity(openURL)
            }
        }



    }

    private fun signInRequest() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                hideLoader()
            }
        }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    updateUI()
                    Log.e("GoogleSign", auth.uid.toString())
                } else {
                    Toast.makeText(this, "Try Again!", Toast.LENGTH_SHORT).show()
                    hideLoader()
                }
            }
    }

    private fun updateUI() {
        checkDbForUserData()
    }

    private fun checkDbForUserData() {
        viewModel.getCurrentUser()
        viewModel.curUser.observe(this, Observer {
            if (it.userId != auth.uid) {
                checkRemote()
            } else {
                viewModel.login()
                navigateToHomeScreen()
            }
        })
    }

    private fun checkRemote() {
        auth.uid?.let { uid ->
            viewModel.getUser(uid).observe(this, Observer { result ->
                when (result) {
                    is Result.Success -> {
                        if (result.data == null) {
                            navigateToDetailsScreen()
                        } else if (result.data.userId == uid) {
                            viewModel.saveUser(
                                result.data.userId,
                                result.data.name,
                                result.data.location,
                                result.data.imgUrl,
                                result.data.about
                            )
                            Log.e("GoogleSignIn", "saveUser")
                        } else {
                            Log.e("GoogleSignIn", "navigateToDetailsScreen")
                            navigateToDetailsScreen()
                        }
                    }

                    is Result.Loading -> {
                        Log.e("GoogleSignIn", "Loading")
                        showLoader()
                    }

                    is Result.Error -> {
                        Log.e("GoogleSignIn", "Error")
                        hideLoader()
                        Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT)
                            .show()
                    }

                    else -> {
                        Log.e("GoogleSignIn", "else")
                        hideLoader()
                    }
                }
            })
        }
    }

    private fun showLoader() {
        binding.progCons.visibility = View.VISIBLE
        binding.mainCons.visibility = View.INVISIBLE
    }

    private fun hideLoader() {
        binding.progCons.visibility = View.INVISIBLE
        binding.mainCons.visibility = View.VISIBLE
    }

    private fun navigateToDetailsScreen() {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

    private fun navigateToHomeScreen() {
        checkDynamicLink()
    }

    private fun homeScreenRoute(deepLink: Uri?) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.putExtra("deepLink", deepLink.toString())
        startActivity(intent)
        finish()
    }

    fun checkDynamicLink() {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData: PendingDynamicLinkData? ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                }
                homeScreenRoute(deepLink)
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