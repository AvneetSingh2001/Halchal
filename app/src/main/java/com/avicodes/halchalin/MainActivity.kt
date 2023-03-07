package com.avicodes.halchalin

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.avicodes.halchalin.databinding.ActivityMainBinding
import com.avicodes.halchalin.presentation.HalchalApp
import com.avicodes.halchalin.presentation.ui.home.HomeActivity
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        var mainActivity: MainActivity? = null

        fun getInstance(): MainActivity? = mainActivity
    }


    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    @Inject
    lateinit var viewModelFactory: MainActivityViewModelFactory

    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = this
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    override fun onResume() {
        super.onResume()
        mainActivity = this
    }

    override fun onRestart() {
        super.onRestart()
        mainActivity = this
    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivity = null
    }

    fun moveToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

}