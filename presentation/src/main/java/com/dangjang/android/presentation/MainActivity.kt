package com.dangjang.android

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.dangjang.android.presentation.IntroFragment
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.btnIntro.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_view, IntroFragment())
                .commit()
        }
    }
}