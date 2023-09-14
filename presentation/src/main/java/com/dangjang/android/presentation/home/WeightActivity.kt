package com.dangjang.android.presentation.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.dangjang.android.domain.constants.ACCESS_TOKEN_KEY
import com.dangjang.android.domain.constants.TOKEN_SPF_KEY
import com.dangjang.android.presentation.R
import com.dangjang.android.presentation.databinding.ActivityWeightBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeightActivity : FragmentActivity() {
    private lateinit var binding: ActivityWeightBinding
    private lateinit var viewModel: HomeViewModel
    private var originWeight: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weight)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_weight)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        getAccessToken()?.let { viewModel.getWeight(it) }

        binding.weightEditBtn.setOnClickListener {
            originWeight = binding.weightTv.text.toString().toInt()

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

            if (originWeight == 0) {
                viewModel.setWeightUnit(binding.weightEt.text.toString())
                getAccessToken()?.let { viewModel.addWeight(it) }
            } else {
                viewModel.setEditWeightUnit(binding.weightEt.text.toString())
                getAccessToken()?.let {  viewModel.editWeight(it) }
            }

        }

        binding.backIv.setOnClickListener {
            finish()
        }

        binding.weightInfoIv.setOnClickListener {
            WeightDialogFragment().show(supportFragmentManager, "WeightDialogFragment")
        }
    }

    private fun getAccessToken(): String? {
        val sharedPreferences = getSharedPreferences(TOKEN_SPF_KEY, Context.MODE_PRIVATE)

        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

}