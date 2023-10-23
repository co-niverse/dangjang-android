package com.dangjang.android.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dangjang.android.domain.constants.NEW_USER_KEY
import com.dangjang.android.presentation.databinding.ActivityMainBinding
import com.dangjang.android.presentation.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private lateinit var binding: ActivityMainBinding
    private var startTime: Double = 0.0
    private var endTime: Double = 0.0
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        startTime = System.currentTimeMillis().toDouble()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavView.setupWithNavController(navController)
        binding.bottomNavView.itemIconTintList = null
    }

    override fun onDestroy() {
        super.onDestroy()
        endTime = System.currentTimeMillis().toDouble()

        if (getNewUserSpf() == "newUser") {
            viewModel.shotSignupAfterTimeLogging((endTime - startTime))
            removeNewUserSpf()
        }
    }

    private fun getNewUserSpf(): String? {
        val sharedPreferences = getSharedPreferences(NEW_USER_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(NEW_USER_KEY, null)
    }

    private fun removeNewUserSpf() {
        val sharedPreferences = getSharedPreferences(NEW_USER_KEY, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(NEW_USER_KEY)
        editor.apply()
    }
}