package com.dangjang.android.presentation.mypage

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.ActivityPointBinding
import com.dangjang.android.presentation.home.GiftListAdapter

class PointActivity : FragmentActivity() {
    private lateinit var binding: ActivityPointBinding
    private val viewModel by viewModels<MypageViewModel>()
    private lateinit var giftListAdapter: GiftListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_point)
        binding.vm = viewModel

        binding.lifecycleOwner = this

        setGiftListAdapter()

        binding.backIv.setOnClickListener {
            finish()
        }
    }

    private fun setGiftListAdapter() {
        giftListAdapter = GiftListAdapter(viewModel)
        binding.pointGiftRv.adapter = giftListAdapter
    }
}