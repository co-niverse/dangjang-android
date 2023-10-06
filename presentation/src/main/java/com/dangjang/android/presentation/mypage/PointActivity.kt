package com.dangjang.android.presentation.mypage

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.ActivityPointBinding
import com.dangjang.android.presentation.home.GiftListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class PointActivity : FragmentActivity() {
    private lateinit var binding: ActivityPointBinding
    private val viewModel by viewModels<MypageViewModel>()
    private lateinit var giftListAdapter: GiftListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_point)
        binding.vm = viewModel

        getAccessToken()?.let { viewModel.getPoint(it) }

        binding.lifecycleOwner = this

        setGiftListAdapter()

        lifecycleScope.launchWhenStarted {
            viewModel.getPointFlow.collectLatest {
                giftListAdapter.submitList(it.products)
            }
        }

        binding.backIv.setOnClickListener {
            finish()
        }

        binding.nextBtn.setOnClickListener {
            binding.coinCl.elevation = 0f
            val pointPhoneFragment = PointPhoneFragment()
            supportFragmentManager.beginTransaction().replace(R.id.point_cl, pointPhoneFragment).addToBackStack(null).commit()
        }
    }

    private fun setGiftListAdapter() {
        giftListAdapter = GiftListAdapter(viewModel)
        binding.pointGiftRv.adapter = giftListAdapter
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = applicationContext.getSharedPreferences(TOKEN_SPF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }
}