package com.avicodes.halchalin.presentation.ui.auth.providers.google

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.utils.Constants.REQ_ONE_TAP
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.databinding.FragmentGoogleAuthBinding
import com.avicodes.halchalin.presentation.ui.auth.MainActivity
import com.avicodes.halchalin.presentation.ui.auth.MainActivityViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GoogleAuthFragment : Fragment() {

    private var _binding: FragmentGoogleAuthBinding? = null
    private val binding get() = _binding!!

    private lateinit var googleSignInClient : GoogleSignInClient

    private val viewModel by activityViewModels<MainActivityViewModel>()

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGoogleAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        activity?.let {
            googleSignInClient = GoogleSignIn.getClient(it, gso)
        }


        binding.apply {
            btnGoogleSignIn.setOnClickListener {
                signInRequest()
                binding.progCons.visibility = View.VISIBLE
                binding.mainCons.visibility = View.INVISIBLE
            }
        }
    }

    private fun signInRequest() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(context, "Failed" , Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    updateUI()
                } else {
                    Toast.makeText(context, "Try Again!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUI() {
         checkDbForUserData()
    }

    private fun checkDbForUserData() {
        viewModel.getCurrentUser()
        viewModel.curUser.observe(viewLifecycleOwner, Observer {
            if (it.userId != auth.uid) {
                checkRemote()
                Log.e("GoogleSignIn", "check remote")
            } else {
                viewModel.login()
                Log.e("GoogleSignIn", "navigateToHomeScreen")
                navigateToHomeScreen()
            }
        })
    }

    private fun checkRemote() {
        auth.uid?.let { uid ->
                viewModel.getUser(uid).observe(viewLifecycleOwner, Observer { result ->
                    when (result) {
                        is Result.Success -> {
                            if (result.data == null) {
                                Log.e("GoogleSignIn", "navigateToDetailsScreen")
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
                            binding.progCons.visibility = View.VISIBLE
                            binding.mainCons.visibility = View.INVISIBLE
                        }

                        is Result.Error -> {
                            Log.e("GoogleSignIn", "Error")
                            binding.progCons.visibility = View.INVISIBLE
                            binding.mainCons.visibility = View.VISIBLE
                            Toast.makeText(requireActivity(), "Try Again", Toast.LENGTH_SHORT)
                                .show()
                        }

                        else -> {
                            Log.e("GoogleSignIn", "else")
                            binding.progCons.visibility = View.INVISIBLE
                            binding.mainCons.visibility = View.VISIBLE
                        }
                    }
                })
            }
    }

    fun navigateToDetailsScreen() {
        val action = GoogleAuthFragmentDirections.actionGoogleAuthFragmentToDetailsFragment()
        requireView().findNavController().navigate(action)
    }

    fun navigateToHomeScreen() {
        (activity as MainActivity).moveToHomeActivity()
    }

}