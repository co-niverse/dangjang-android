package com.dangjang.android.presentation.home

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.ActivityGlucoseBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GlucoseActivity : FragmentActivity() {
    private lateinit var binding: ActivityGlucoseBinding
    private lateinit var viewModel: GlucoseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glucose)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProvider(this).get(GlucoseViewModel::class.java)

    }

}