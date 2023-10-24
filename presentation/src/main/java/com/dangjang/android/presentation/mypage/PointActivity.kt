package com.dangjang.android.presentation.mypage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.domain.model.ProductVO
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.ActivityPointBinding
import com.dangjang.android.presentation.home.GiftListAdapter
import com.dangjang.android.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PointActivity : FragmentActivity() {
    private lateinit var binding: ActivityPointBinding
    private val viewModel by viewModels<MypageViewModel>()
    private lateinit var giftListAdapter: GiftListAdapter
    private lateinit var pointManualListAdapter: PointManualListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_point)
        binding.vm = viewModel

        getAccessToken()?.let { viewModel.getPoint(it) }

        lifecycleScope.launchWhenStarted {
            viewModel.goToLoginActivityFlow.collectLatest {
                if (it) {
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        binding.lifecycleOwner = this

        setGiftListAdapter()
        setPointManualListAdapter()

        binding.nextBtn.setOnTouchListener({ v, event -> true })

        lifecycleScope.launchWhenStarted {
            viewModel.getPointFlow.collectLatest {
                giftListAdapter.submitList(it.products)
            }
        }

        lifecycleScope.launch {
            viewModel.getPointFlow.collectLatest {
                Log.e("point",it.descriptionListToEarnPoint.toString())
                pointManualListAdapter.submitList(it.descriptionListToEarnPoint)
            }
        }

        binding.backIv.setOnClickListener {
            finish()
        }

        binding.nextBtn.setOnClickListener {
            binding.coinCl.elevation = 0f
            val pointPhoneFragment = PointPhoneFragment()
            supportFragmentManager.beginTransaction().replace(R.id.point_top_cl, pointPhoneFragment).addToBackStack(null).commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.shotPointExposureLogging()
    }

    private fun setGiftListAdapter() {
        giftListAdapter = GiftListAdapter(viewModel)
        giftListAdapter.setMyItemClickListener(object :
            GiftListAdapter.MyItemClickListener {
            override fun onItemClick(giftListItem: ProductVO) {
                viewModel.setSelectedGiftTitle(giftListItem.title)
                viewModel.setSelectedGiftPrice(giftListItem.price.toString())
                setBtnGreen()
//                if (giftListItem.price <= viewModel.getPointFlow.value.balancedPoint) {
//                    setBtnGreen()
//                } else {
//                    Toast.makeText(applicationContext,"포인트가 부족합니다.", Toast.LENGTH_SHORT).show()
//                }
            }
        })
        binding.pointGiftRv.adapter = giftListAdapter
    }

    private fun setPointManualListAdapter() {
        pointManualListAdapter = PointManualListAdapter(viewModel)
        binding.pointManualRv.adapter = pointManualListAdapter
    }

    private fun setBtnGreen() {
        binding.nextBtn.setBackgroundResource(R.drawable.background_green_gradient)
        binding.nextBtn.setOnTouchListener({ v, event -> false })
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = applicationContext.getSharedPreferences(TOKEN_SPF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }
}