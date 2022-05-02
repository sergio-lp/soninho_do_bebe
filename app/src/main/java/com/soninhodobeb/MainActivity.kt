package com.soninhodobeb

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.soninhodobeb.databinding.ActivityMainBinding
import com.soninhodobeb.helpers.ErrorHelper
import com.soninhodobeb.ui.premium.PremiumViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel = PremiumViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition {
            if (viewModel.isLoading.value) {
                return@setKeepOnScreenCondition true
            } else {
                binding = ActivityMainBinding.inflate(layoutInflater)

                val navView: BottomNavigationView = binding.navView
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
                navHostFragment?.findNavController().let {
                    val navController = it ?: run {
                        ErrorHelper.showSnackbar(binding.root)
                        return@setKeepOnScreenCondition false
                    }
                    val appBarConfiguration = AppBarConfiguration(
                        setOf(
                            R.id.navigation_sounds,
                            R.id.navigation_premium,
                            R.id.navigation_settings
                        )
                    )
                    setupActionBarWithNavController(navController, appBarConfiguration)
                    navView.setupWithNavController(navController)
                }

                setContentView(binding.root)

                return@setKeepOnScreenCondition false
            }
        }
    }
}