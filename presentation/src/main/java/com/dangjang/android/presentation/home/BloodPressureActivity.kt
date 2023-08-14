package com.dangjang.android.presentation.home

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.ActivityBloodPressureBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BloodPressureActivity : FragmentActivity() {
    private lateinit var binding: ActivityBloodPressureBinding
    private lateinit var viewModel: BloodPressureViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blood_pressure)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_blood_pressure)
        viewModel = ViewModelProvider(this).get(BloodPressureViewModel::class.java)

        binding.bloodPressureEditBtn.setOnClickListener {
            binding.systolicEditView.visibility = View.VISIBLE
            binding.systolicTv.visibility = View.GONE
            binding.systolicEt.visibility = View.VISIBLE

            binding.diastolicEditView.visibility = View.VISIBLE
            binding.diastolicTv.visibility = View.GONE
            binding.diastolicEt.visibility = View.VISIBLE

            binding.bloodPressureEditBtn.visibility = View.GONE
            binding.bloodPressureOkBtn.visibility = View.VISIBLE
        }

        binding.bloodPressureOkBtn.setOnClickListener {
            binding.systolicEditView.visibility = View.GONE
            binding.systolicTv.visibility = View.VISIBLE
            binding.systolicEt.visibility = View.GONE

            binding.diastolicEditView.visibility = View.GONE
            binding.diastolicTv.visibility = View.VISIBLE
            binding.diastolicEt.visibility = View.GONE

            binding.bloodPressureEditBtn.visibility = View.VISIBLE
            binding.bloodPressureOkBtn.visibility = View.GONE
        }
    }

}