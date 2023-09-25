package com.dangjang.android.presentation.mypage

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.ActivityPointBinding

class PointActivity : FragmentActivity() {
    private lateinit var binding: ActivityPointBinding
    private val viewModel by viewModels<MypageViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point)
    }
}