package com.nasa.apod.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.nasa.apod.R
import com.nasa.apod.databinding.ActivityMainBinding
import com.nasa.apod.presentation.utils.SetTheme
import com.nasa.apod.presentation.utils.ThemeHelper
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // light - true
        // dark - false
        when(AppCompatDelegate.getDefaultNightMode()){
            AppCompatDelegate.MODE_NIGHT_YES -> { // set light
                binding.checkBoxThemeMode.isChecked = true
            }
            else -> { // set night
                binding.checkBoxThemeMode.isChecked = false
            }
        }

        binding.checkBoxThemeMode.setOnCheckedChangeListener { buttonView, isChecked ->
            when(AppCompatDelegate.getDefaultNightMode()){
                AppCompatDelegate.MODE_NIGHT_YES -> { // set light
                    ThemeHelper().applyTheme(SetTheme.LIGHT)
                }
                else -> { // set night
                    ThemeHelper().applyTheme(SetTheme.DARK)
                }
            }
        }
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}