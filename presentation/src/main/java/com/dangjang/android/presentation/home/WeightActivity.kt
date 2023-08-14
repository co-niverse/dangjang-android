package com.dangjang.android.presentation.home

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.ActivityWeightBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeightActivity : FragmentActivity() {
    private lateinit var binding: ActivityWeightBinding
    private lateinit var viewModel: WeightViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weight)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_weight)
        viewModel = ViewModelProvider(this).get(WeightViewModel::class.java)

        binding.weightEditBtn.setOnClickListener {
            binding.weightEditView.visibility = View.VISIBLE
            binding.weightTv.visibility = View.GONE
            binding.weightEt.visibility = View.VISIBLE

            binding.weightEditBtn.visibility = View.GONE
            binding.weightOkBtn.visibility = View.VISIBLE
        }

        binding.weightOkBtn.setOnClickListener {
            binding.weightEditView.visibility = View.GONE
            binding.weightTv.visibility = View.VISIBLE
            binding.weightEt.visibility = View.GONE

            binding.weightEditBtn.visibility = View.VISIBLE
            binding.weightOkBtn.visibility = View.GONE
        }

    }

}