package com.dangjang.android.presentation.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.ActivityAlarmBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmActivity : FragmentActivity() {
    private lateinit var binding: ActivityAlarmBinding
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var alarmListAdapter: AlarmListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_alarm)
        binding.lifecycleOwner = this

        setAlarmListAdapter()

        lifecycleScope.launchWhenStarted {
//            viewModel.getAlarmFlow.collectLatest {
//                alarmListAdapter.submitList()
//            }
        }

        binding.backIv.setOnClickListener {
            finish()
        }
    }

    private fun setAlarmListAdapter() {
        alarmListAdapter = AlarmListAdapter(viewModel)
        binding.alarmRv.adapter = alarmListAdapter
    }

}