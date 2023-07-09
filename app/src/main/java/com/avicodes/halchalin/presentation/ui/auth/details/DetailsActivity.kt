package com.avicodes.halchalin.presentation.ui.auth.details

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.avicodes.halchalin.R
import com.avicodes.halchalin.data.models.City
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.databinding.ActivityDetailsBinding
import com.avicodes.halchalin.presentation.ui.auth.providers.google.GoogleAuthActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private var _binding: ActivityDetailsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: DetailsActivityViewModelFactory

    @Inject
    lateinit var auth: FirebaseAuth

    lateinit var viewModel: DetailsActivityViewModel

    private var citiesList: List<String> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout for this fragment
        _binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel =
            ViewModelProvider(this, factory)[DetailsActivityViewModel::class.java]

        getCities()

        binding.apply {

            btnContinue.setOnClickListener {
                val name = etName.editText?.text.toString()
                val loc = etLoc.editText?.text.toString()

                if (name != "" && loc != "") {
                    viewModel.saveUser(
                        name = name,
                        loc = loc,
                    )
                    viewModel.login()
                    navigateToHome()

                } else {
                    if (name == "") {
                        etName.error = "Required"
                    }
                }
            }

            btnGoogleChange.setOnClickListener {
                auth.signOut()
                navigateToGoogleSignInScreen()
            }
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

    private fun navigateToGoogleSignInScreen() {
        val intent = Intent(this, GoogleAuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

    fun getCities() {
        viewModel.getCities().observe( this, Observer {
            when (it) {
                is Result.Success -> {
                    citiesList = it.data?.map(City::name)!!
                    Log.e("Cities Fetch", it.data.toString())
                    val adapter = ArrayAdapter(this, R.layout.item_city, citiesList)
                    (binding.etLoc.editText as? AutoCompleteTextView)?.setAdapter(adapter)
                }

                is Result.Error -> {
                    Log.e("Cities Fetch", it.exception?.message.toString())
                    val items = listOf("Error Fetching Data")
                    val adapter = ArrayAdapter( this, R.layout.item_city, items)
                    (binding.etLoc.editText as? AutoCompleteTextView)?.setAdapter(adapter)

                }
                is Result.Loading -> {
                    val items = listOf("Loading...")
                    val adapter = ArrayAdapter(this, R.layout.item_city, items)
                    (binding.etLoc.editText as? AutoCompleteTextView)?.setAdapter(adapter)
                }
                else -> {}
            }
        })
    }
}